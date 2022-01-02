package lollipop;

import awatch.AParser;
import awatch.models.Anime;
import awatch.models.Article;
import mread.controller.RClient;
import mread.controller.RListener;
import mread.controller.RParser;
import mread.model.Chapter;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//api path: https://www.readm.org/

public class MangaAPI implements RListener {

    public static String apiPath = "https://www.readm.org";
    public static String malApi = "https://api.jikan.moe/v4";
    private ArrayList<Manga> mangaSearched = new ArrayList<>();
    RClient api = new RClient(this);
    private Message messageToEdit = null;

    public void searchMangas(String query, Message c) {
        api.search(query);
        messageToEdit = c;
    }

    @Override
    public void setMangas(List<Manga> mangas) {
        if(mangas.isEmpty()) { messageToEdit.editMessageEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Could not find any results with that search query! Please try again with a valid manga!").build()).queue(); return; }
        for(Manga manga : mangas) mangaSearched.add(manga);
        messageToEdit.editMessageEmbeds(Tools.mangaToEmbed(mangas.get(0)).build()).queue();
    }

    @Override
    public void setChapters(Manga manga) {
        for (Chapter c : manga.chapters) {
            System.out.println(c);
        }
        api.pages(manga.chapters.get(0));
    }

    @Override
    public void setPages(Chapter chapter) {
        for (String s : chapter.pages) {
            System.out.println(s);
        }
    }

    public ArrayList<Article> mangaNews(long id) throws IOException {
        Article r = new Article();
        URL web = new URL(malApi+"/manga/" + id + "/news");
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return RParser.getNews(data);
    }

}
