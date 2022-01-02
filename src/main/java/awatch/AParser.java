package awatch;

import awatch.models.Anime;
import awatch.models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AParser {

    public static ArrayList<Anime> parseData(JSONObject data) {
        ArrayList<Anime> animes = new ArrayList<>();
        JSONArray arr = null;
        try {
            arr = data.getJSONArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            JSONObject result = arr.getJSONObject(i);
            Anime anime = new Anime();
            try { anime.art = result.getJSONObject("images").getJSONObject("jpg").getString("image_url"); } catch(Exception ignored) {}
            try { anime.malID = result.getInt("mal_id"); } catch(Exception ignored) {}
            try { anime.status = result.getString("status"); } catch(Exception ignored) {}
            try { anime.rating = result.getString("rating"); } catch(Exception ignored) {}
            try { anime.score = result.getDouble("score"); } catch(Exception ignored) {}
            try { anime.summary = result.getString("synopsis"); } catch(Exception ignored) {}
            try { anime.title = result.getString("title"); } catch(Exception ignored) {}
            try { anime.url = result.getString("url"); } catch(Exception ignored) {}
            try { anime.rank = result.getInt("rank"); } catch(Exception ignored) {}
            try { anime.type = result.getString("type"); } catch(Exception ignored) {}
            try { anime.trailer = result.getJSONObject("trailer").getString("url"); } catch(Exception ignored) { anime.trailer = "Unkown"; }
            try { anime.episodeCount = result.getInt("episodes"); } catch(Exception ignored) {}
            animes.add(anime);
        }
        return animes;
    }

    public static ArrayList<Anime> parseTop(JSONObject data) {
        ArrayList<Anime> animes = new ArrayList<>();
        JSONArray arr = null;
        try {
            arr = data.getJSONArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<10; i++) {
            JSONObject result = arr.getJSONObject(i);
            Anime anime = new Anime();
            try { anime.art = result.getJSONObject("images").getJSONObject("jpg").getString("image_url"); } catch(Exception ignored) {}
            try { anime.malID = result.getInt("mal_id"); } catch(Exception ignored) {}
            try { anime.status = result.getString("status"); } catch(Exception ignored) {}
            try { anime.rating = result.getString("rating"); } catch(Exception ignored) {}
            try { anime.score = result.getDouble("score"); } catch(Exception ignored) {}
            try { anime.summary = result.getString("synopsis"); } catch(Exception ignored) {}
            try { anime.title = result.getString("title"); } catch(Exception ignored) {}
            try { anime.url = result.getString("url"); } catch(Exception ignored) {}
            try { anime.rank = result.getInt("rank"); } catch(Exception ignored) {}
            try { anime.type = result.getString("type"); } catch(Exception ignored) {}
            try { anime.trailer = result.getJSONObject("trailer").getString("url"); } catch(Exception ignored) { anime.trailer = "Unkown"; }
            try { anime.episodeCount = result.getInt("episodes"); } catch(Exception ignored) {}
            animes.add(anime);
        }
        return animes;
    }

    public static Anime parseAnime(JSONObject data) {
        Anime anime = new Anime();
        JSONObject result = null;
        try {
            result = data.getJSONObject("data");
        } catch(Exception e) { return null; }
        try { anime.art = result.getJSONObject("images").getJSONObject("jpg").getString("image_url"); } catch(Exception ignored) {}
        try { anime.malID = result.getInt("mal_id"); } catch(Exception ignored) {}
        try { anime.status = result.getString("status"); } catch(Exception ignored) {}
        try { anime.rating = result.getString("rating"); } catch(Exception ignored) {}
        try { anime.score = result.getDouble("score"); } catch(Exception ignored) {}
        try { anime.summary = result.getString("synopsis"); } catch(Exception ignored) {}
        try { anime.title = result.getString("title"); } catch(Exception ignored) {}
        try { anime.url = result.getString("url"); } catch(Exception ignored) {}
        try { anime.rank = result.getInt("rank"); } catch(Exception ignored) {}
        try { anime.type = result.getString("type"); } catch(Exception ignored) {}
        try { anime.trailer = result.getJSONObject("trailer").getString("url"); } catch(Exception ignored) { anime.trailer = "Unkown"; }
        try { anime.episodeCount = result.getInt("episodes"); } catch(Exception ignored) {}
        return anime;
    }

    public static ArrayList<Article> getNews(JSONObject data) {
        ArrayList<Article> articles = new ArrayList<>();
        JSONArray arr = null;
        try {
            arr = data.getJSONArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            JSONObject res = arr.getJSONObject(i);
            Article a = new Article();
            try { a.author = res.getString("author_username"); } catch(Exception ignored) {}
            try { a.authorUrl = res.getString("author_url"); } catch(Exception ignored) {}
            try { a.url = res.getString("url"); } catch(Exception ignored) {}
            try { a.title = res.getString("title"); } catch(Exception ignored) {}
            try { a.comments = res.getInt("comments"); } catch(Exception ignored) {}
            try { a.date = res.getString("date"); } catch(Exception ignored) {}
            try { a.desc = res.getString("excerpt"); } catch(Exception ignored) {}
            try { a.forum = res.getString("forum_url"); } catch(Exception ignored) {}
            try { a.image = res.getJSONObject("images").getJSONObject("jpg").getString("image_url"); } catch(Exception ignored) {}
            articles.add(a);
        }
        return articles;
    }

}
