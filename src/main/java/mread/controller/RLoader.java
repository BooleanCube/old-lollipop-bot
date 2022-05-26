package mread.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import awatch.model.Anime;
import lollipop.API;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import mread.model.Chapter;
import mread.model.Manga;

public class RLoader {

    // Manga Cache
    public static HashMap<String, ArrayList<Manga>> mangaCache = new HashMap<>();

	public static List<Manga> browse(int page, String genre) {
		List<Manga> mangaList = new ArrayList<>();
		try {
			String pageUrl;
			if (genre == null) pageUrl = RApiBuilder.buildBrowse(page);
			else pageUrl = RApiBuilder.buildCatBrowse(page, genre);
			Element doc = Jsoup.connect(pageUrl).userAgent(RConstants.USER_AGENT).get().body();
			for (Element manga : doc.select("li[class=mb-lg]")) {
				String title = manga.select("div[class=subject-title]").select("a").attr("title");
				String url = manga.select("div[class=subject-title]").select("a").attr("href");
				String summary = manga.select("p[class=desktop-only excerpt]").text();
				String[] data = manga.select("div[class=color-imdb]").text().split(" ");
				String rating = null;
				if(data.length >= 2) rating = data[1];
				String art = manga.select("div[class=poster-with-subject]").select("img").attr("src");
				List<String> tags = new ArrayList<>();
				for (Element tag : manga.select("span[class=genres]").select("a")) { tags.add(tag.attr("title")); }
				Manga m = new Manga(title, url, summary, rating, art, tags);
				mangaList.add(m);
			}
		} catch (IOException e) { e.printStackTrace(); }

		return mangaList;
	}

	public static Manga getChapters(Manga manga) {
		List<Chapter> chapterList = new ArrayList<>();
		String author = null, status = null;

		try {

			Element doc = Jsoup.connect(RApiBuilder.buildCombo(manga.url)).userAgent(RConstants.USER_AGENT).get().body();
			
			author = doc.select("div[class=sub-title pt-sm]").text();
			status = doc.select("span[class=series-status aqua]").text();
			
			for (Element chp : doc.select("section[class=episodes-box]").select("table[class=ui basic unstackable table]")) {
				String title = chp.select("a").text();
				String url = chp.select("a").attr("href");
				String pub = chp.select("td[class=episode-date]").text();
				Chapter chapter = new Chapter(title, url, pub, null);
				chapterList.add(chapter);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		manga.chapters = chapterList;
		manga.author = author;
		manga.status = status;
		manga.chapter = String.valueOf(chapterList.size());
		return manga;
	}

	public static Chapter getPages(Chapter chapter) {
		List<String> pages = new ArrayList<>();
		try {
			Element doc = Jsoup.connect(RApiBuilder.buildCombo(chapter.url)).userAgent(RConstants.USER_AGENT).get().body();
			for (Element pg : doc.select("img[class=img-responsive scroll-down]")) {
				String page = pg.attr("src");
				pages.add(page);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		chapter.pages = pages;
		return chapter;
	}

	public static List<Manga> search(String query) {
        if(mangaCache.containsKey(query)) return mangaCache.get(query);
		ArrayList<Manga> mangaList = new ArrayList<>();
		try {
			HashMap<String, String> data = new HashMap<>();
			data.put("dataType", "json");
			data.put("phrase", query);
			HashMap<String, String> headers = new HashMap<>();
			headers.put("X-Requested-With", "XMLHttpRequest");
			String doc = Jsoup.connect("https://www.readm.org/service/search").timeout(RConstants.TIMEOUT)
					.userAgent(RConstants.USER_AGENT).ignoreHttpErrors(true).headers(headers).data(data)
					.ignoreContentType(true).post().select("body").text();
			DataObject json = DataObject.fromJson(doc);
			DataArray array;
			try { array = json.getArray("manga"); } catch(Exception e) { return mangaList; }
			for (int i = 0; i < array.length(); i++) {
				DataObject obj = array.getObject(i);
				String title = "Unkown", url = null, art = null;
				ArrayList<String> tokens = new ArrayList<>();
				if(obj.hasKey("title")) title = obj.getString("title");
				if(obj.hasKey("url")) url = obj.getString("url");
				if(obj.hasKey("image")) art = obj.getString("image");
				if(obj.hasKey("tokens")) {
					DataArray tokensArr = obj.getArray("tokens");
					for(int j=0; j<tokensArr.length(); j++) tokens.add(tokensArr.getString(j));
				}
				Manga m = new Manga(title, url, "summary", "0", art, tokens);
				mangaList.add(m);

				// This limits the search results to only 1
				// I only did this for time efficiency because otherwise it would take ages iterating and web scraping over all the search results.
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for(Manga manga : mangaList) {
			List<Chapter> chapterList = new ArrayList<>();
			String author = "Unkown", status = "Unkown", summary = "Summary not found!", rating = "Unknown";
			int chapterCount = -1;
			try {
				Element doc = Jsoup.connect(RApiBuilder.buildCombo(manga.url)).userAgent(RConstants.USER_AGENT).get()
						.body();
				author = doc.select("div[class=first_and_last]").text();
				status = doc.select("span[class=series-status aqua]").text();
				chapterCount = doc.select("section[class=episodes-box]").select("table[class=ui basic unstackable table]").size();
				summary = doc.select("div[class=series-summary-wrapper]").text();
				rating = doc.select("div[class=color-imdb]").text();
			} catch (IOException e) {
				e.printStackTrace();
			}
			manga.author = author;
			manga.status = status;
			if(manga.status.equalsIgnoreCase("ongoing")) manga.status = "On Going";
			else if(manga.status.equalsIgnoreCase("completed")) manga.status = "Completed";
			else manga.status = "Unknown";
			manga.chapter = Integer.toString(chapterCount);
			manga.summary = summary;
			manga.rating = rating;
		}
		mangaCache.put(query, mangaList);
		return mangaList;
	}

}
