package lollipop;

import awatch.*;

import awatch.models.Character;
import awatch.models.Anime;
import awatch.models.Article;
import awatch.models.Statistic;
import lollipop.commands.Statistics;
import mread.controller.RClient;
import mread.controller.RListener;
import mread.controller.RParser;
import mread.model.Chapter;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class API implements RListener {

    static String v4API = "https://api.jikan.moe/v4";
    static String v3API = "https://api.jikan.moe/v3";
    static String quoteAPI = "https://animechan.vercel.app/api/random";
    public static String readmAPI = "https://www.readm.org";

    //manga
    private ArrayList<Manga> mangaSearched = new ArrayList<>();
    RClient mAPI = new RClient(this);
    private Message messageToEdit = null;

    public void searchMangas(String query, Message c) {
        mAPI.search(query);
        messageToEdit = c;
    }

    @Override
    public void setMangas(List<Manga> mangas) {
        if(mangas.isEmpty()) { messageToEdit.editMessageEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Could not find any results with that search query! Please try again with a valid manga!").build()).queue(); return; }
        mangaSearched.addAll(mangas);
        messageToEdit.editMessageEmbeds(Tools.mangaToEmbed(mangas.get(0)).build()).queue();
    }

    @Override
    public void setChapters(Manga manga) {
        for (Chapter c : manga.chapters) System.out.println(c);
        mAPI.pages(manga.chapters.get(0));
    }

    @Override
    public void setPages(Chapter chapter) {
        for (String s : chapter.pages) System.out.println(s);
    }

    public ArrayList<Article> mangaNews(long id) throws IOException {
        URL web = new URL(v4API+"/manga/" + id + "/news");
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

    //quote
    public static void sendQuote(MessageChannel c) throws IOException {
        URL web = new URL(quoteAPI);
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000); // Sets Connection Timeout to 5 seconds
        con.setReadTimeout(5000); // Sets Connection Timeout to 5 seconds
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        String anime = data.getString("anime");
        String character = data.getString("character");
        String quote = data.getString("quote");
        c.sendMessageEmbeds(
                new EmbedBuilder()
                        .setDescription("\"" + quote + "\"\n-" + character)
                        .setFooter("from " + anime)
                        .build()
        ).queue();
    }

    //anime and character
    public ArrayList<Article> animeNews(long id) throws IOException {
        URL web = new URL(v4API+"/anime/" + id + "/news");
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000); // Sets Connection Timeout to 5 seconds
        con.setReadTimeout(5000); // Sets Read Timeout to 5 seconds
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return AParser.getNews(data);
    }

    public ArrayList<Anime> searchForAnime(String query, boolean nsfw) throws IOException {
        String extension = !nsfw ? "&sfw=true" : "";
        URL web = new URL(v4API+"/anime?q=" + query.replaceAll(" ", "%20") + extension);
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000); // Sets Connection Timeout to 5 seconds
        con.setReadTimeout(5000); // Sets Read Timeout to 5 seconds
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return AParser.parseData(data);
    }

    public ArrayList<Anime> topAnime() throws IOException {
        URL web = new URL(v4API+"/top/anime");
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000); // Sets Connection Timeout to 5 seconds
        con.setReadTimeout(5000); // Sets Read Timeout to 5 seconds
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return AParser.parseTop(data);
    }

    public Anime randomAnime(boolean nsfw) throws IOException {
        String extension = !nsfw ? "&sfw=true" : "";
        URL web = new URL(v4API+"/random/anime" + extension);
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000); // Sets Connection Timeout to 5 seconds
        con.setReadTimeout(5000); // Sets Read Timeout to 5 seconds
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return AParser.parseAnime(data);
    }

    public Character searchForCharacter(String query) throws IOException {
        URL web = new URL(v3API+"/search/character?q=" + query.replaceAll(" ", "%20"));
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return CParser.parseData(data);
    }

    public String pictureCharacter(long id) throws IOException {
        URL web = new URL(v4API+"/characters/" + id + "/pictures");
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return CParser.getRandomPictureChar(data);
    }

    public String pictureAnime(long id) throws IOException {
        URL web = new URL(v4API+"/anime/" + id + "/pictures");
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return CParser.getRandomPicture(data);
    }

    public String pictureManga(long id) throws IOException {
        URL web = new URL(v4API+"/manga/" + id + "/pictures");
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return CParser.getRandomPicture(data);
    }

    public Statistic getAnimeStats(long id) throws IOException {
        URL web = new URL(v4API+"/anime/" + id + "/statistics");
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return AParser.parseStats(data);
    }

}
