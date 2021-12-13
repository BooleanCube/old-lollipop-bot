package lollipop;

import awatch.AParser;
import awatch.Anime;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AnimeAPI {

    public static String apiPath = "https://api.jikan.moe/v3";

    public Anime searchForAnime(String query) throws IOException {
        Anime r = new Anime();
        URL web = new URL(apiPath+"/search/anime?q=" + query.replaceAll(" ", "%20"));
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JSONObject data = new JSONObject(bf.readLine());
        return AParser.parseData(data);
    }

}
