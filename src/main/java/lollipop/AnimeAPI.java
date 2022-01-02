package lollipop;

import awatch.*;

import awatch.models.Character;
import awatch.models.Anime;
import awatch.models.Article;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class AnimeAPI {

    public static String apiPath = "https://api.jikan.moe/v4";

    public ArrayList<Article> animeNews(long id) throws IOException {
        Article r = new Article();
        URL web = new URL(apiPath+"/anime/" + id + "/news");
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

    public ArrayList<Anime> searchForAnime(String query) throws IOException {
        Anime r = new Anime();
        URL web = new URL(apiPath+"/anime?q=" + query.replaceAll(" ", "%20"));
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

    public Character searchForCharacter(String query) throws IOException {
        Character r = new Character();
        URL web = new URL("https://api.jikan.moe/v3/search/character?q=" + query.replaceAll(" ", "%20"));
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
        URL web = new URL(apiPath+"/characters/" + id + "/pictures");
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
        URL web = new URL(apiPath+"/anime/" + id + "/pictures");
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
        URL web = new URL(apiPath+"/manga/" + id + "/pictures");
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

}
