package lollipop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class QuoteAPI {

    public static String apiPath = "https://animechan.vercel.app/api/random";

    public static void sendQuote(MessageChannel c) throws IOException {
        URL web = new URL(apiPath);
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

}
