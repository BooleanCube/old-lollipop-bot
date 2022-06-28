package mread.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import awatch.controller.AConstants;
import awatch.model.Anime;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import mread.model.Chapter;
import mread.model.Manga;

import javax.management.BadAttributeValueExpException;
import javax.net.ssl.HttpsURLConnection;

public class RLoader {

    // Manga Cache
    public static HashMap<String, ArrayList<Manga>> mangaCache = new HashMap<>();

    /**
     * Browse for mangas
     * @param page page number
     * @param genre genre
     * @return list of mangas
     */
	public static ArrayList<Manga> browseManga(int page, String genre) {
		ArrayList<Manga> mangaList = new ArrayList<>();
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
				Manga m = new Manga(title, url, summary, art, tags);
                m.parseData(manga);
				mangaList.add(m);
			}
		} catch (IOException e) { e.printStackTrace(); }

		return mangaList;
	}

    /**
     * get chapters for a specific manga
     * @param manga manga
     * @return list of chapters
     */
	public static ArrayList<Chapter> getChapters(Manga manga) {
		ArrayList<Chapter> chapterList = new ArrayList<>();
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
		manga.author = author;
		manga.status = status;
		manga.chapter = chapterList.size();
		return chapterList;
	}

    /**
     * get pages for chapter
     * @param chapter chapter
     * @return list of pages
     */
	public static ArrayList<String> getPages(Chapter chapter) {
		ArrayList<String> pages = new ArrayList<>();
		try {
			Element doc = Jsoup.connect(RApiBuilder.buildCombo(chapter.url)).userAgent(RConstants.USER_AGENT).get().body();
			for (Element pg : doc.select("img[class=img-responsive scroll-down]")) {
				String page = RConstants.BASE_URL + pg.attr("src");
				pages.add(page);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pages;
    }

    /**
     * search for mangas
     * @param query query
     * @return list of mangas
     */
	public static ArrayList<Manga> searchManga(String query) {
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
			for (int i=0; i < Math.min(5, array.length()); i++) {
				DataObject obj = array.getObject(i);
				String title = "Unkown", url = null, art = null;
				ArrayList<String> tokens = new ArrayList<>();
				if(obj.hasKey("title")) title = obj.getString("title");
				if(obj.hasKey("url")) url = RConstants.BASE_URL + obj.getString("url");
				if(obj.hasKey("image")) art = RConstants.BASE_URL + obj.getString("image");
				if(obj.hasKey("tokens")) {
					DataArray tokensArr = obj.getArray("tokens");
					for(int j=0; j<tokensArr.length(); j++) tokens.add(tokensArr.getString(j));
				}
				Manga m = new Manga(title, url, "summary", art, tokens);
				mangaList.add(m);
			}
		} catch (Exception e) {
			return null;
		}

		for(Manga manga : mangaList) {
			try {
				Element doc = Jsoup.connect(RApiBuilder.buildCombo(manga.url)).userAgent(RConstants.USER_AGENT).get()
						.body();
                manga.parseData(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        mangaList.sort(Comparator.comparingInt(m -> -(int)(m.score*10+m.views)));
		mangaCache.put(query, mangaList);
		return mangaList;
	}

    /**
     * Loads the most popular mangas
     * @return list of popular mangas
     */
    public static ArrayList<Manga> getPopularManga() {
        ArrayList<Manga> popular = new ArrayList<>();

        try {
            Element doc = Jsoup.connect("https://readm.org/popular-manga/").timeout(RConstants.TIMEOUT)
                    .userAgent(RConstants.USER_AGENT).get().body();
            for(Element result : doc.select("div[class=ui mb-lg mt-0]").select("ul[class=filter-results]").select("li[class=mb-lg]")) {
                if(popular.size() >= 20) break;
                Manga manga = new Manga();
                manga.parseRankData(result);
                popular.add(manga);
            }
        } catch(Exception e) {
            return null;
        }

        return popular;
    }

    /**
     * Loads the top rated mangas
     * @return list of top mangas
     */
    public static ArrayList<Manga> getTopManga() {
        ArrayList<Manga> top = new ArrayList<>();

        try {
            Element doc = Jsoup.connect("https://readm.org/popular-manga/rating/").timeout(RConstants.TIMEOUT)
                    .userAgent(RConstants.USER_AGENT).get().body();
            for(Element result : doc.select("div[class=ui mb-lg mt-0]").select("ul[class=filter-results]").select("li[class=mb-lg]")) {
                if(top.size() >= 20) break;
                Manga manga = new Manga();
                manga.parseRankData(result);
                top.add(manga);
            }
        } catch(Exception e) {
            return null;
        }

        return top;
    }

    /**
     * Loads the most popular mangas
     * @return list of popular mangas
     */
    public static ArrayList<Manga> getLatestManga() {
        ArrayList<Manga> latest = new ArrayList<>();

        try {
            Element doc = Jsoup.connect("https://readm.org/latest-releases/").timeout(RConstants.TIMEOUT)
                    .userAgent(RConstants.USER_AGENT).get().body();
            for(Element result : doc.select("div[class=dark-segment]").select("ul[class=clearfix latest-updates]").select("li[class=segment-poster-sm]")) {
                if(latest.size() >= 25) break;
                Manga manga = new Manga();
                manga.parseLatestData(result);
                latest.add(manga);
            }
        } catch(Exception e) {
            return null;
        }

        return latest;
    }

    public static Manga getRandomManga(boolean nsfw) {
        try {
            String extension = !nsfw ? "?sfw" : "";
            URL web = new URL(AConstants.v4API+"/random/manga" + extension);
            HttpsURLConnection con = configureConnection(web);
            BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            DataObject data = DataObject.fromJson(bf.readLine());
            Manga manga = new Manga();
            DataObject result;
            try {
                result = data.getObject("data");
                manga.parseMALData(result);
            } catch(Exception e) { return null; }
            return manga;
        } catch(Exception e) { return null; }
    }

    /**
     * Declare the HttpsURLConnection and configure for REST API
     * @param web rest api url object
     * @return {@link HttpsURLConnection} for the REST API
     * @throws IOException for declaring and configuring {@link HttpsURLConnection}
     */
    public static HttpsURLConnection configureConnection(URL web) throws IOException{
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000); // Sets Connection Timeout to 5 seconds
        con.setReadTimeout(5000); // Sets Read Timeout to 5 seconds
        return con;
    }

}
