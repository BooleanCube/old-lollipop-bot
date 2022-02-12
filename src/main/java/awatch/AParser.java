package awatch;

import awatch.models.Anime;
import awatch.models.Article;
import awatch.models.Statistic;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.ArrayList;

public class AParser {

    public static ArrayList<Anime> parseData(DataObject data) {
        ArrayList<Anime> animes = new ArrayList<>();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject result = arr.getObject(i);
            Anime anime = new Anime();
            anime.art = result.getObject("images").getObject("jpg").getString("image_url", "");
            anime.malID = result.getInt("mal_id", 0);
            anime.status = result.getString("status", "");
            anime.rating = result.getString("rating", "");
            anime.score = result.getDouble("score", 0);
            anime.summary = result.getString("synopsis", "");
            anime.title = result.getString("title", "");
            anime.url = result.getString("url", "");
            anime.rank = result.getInt("rank", Integer.MAX_VALUE);
            anime.type = result.getString("type", "");
            anime.trailer = result.getObject("trailer").getString("url", "");
            anime.episodeCount = result.getInt("episodes", 0);
            anime.popularity = result.getInt("popularity", Integer.MAX_VALUE);
            animes.add(anime);
        }
        return animes;
    }

    public static ArrayList<Anime> parseTop(DataObject data) {
        ArrayList<Anime> animes = new ArrayList<>();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<10; i++) {
            DataObject result = arr.getObject(i);
            Anime anime = new Anime();
            anime.art = result.getObject("images").getObject("jpg").getString("image_url", "");
            anime.malID = result.getInt("mal_id", 0);
            anime.status = result.getString("status", "");
            anime.rating = result.getString("rating", "");
            anime.score = result.getDouble("score", 0);
            anime.summary = result.getString("synopsis", "");
            anime.title = result.getString("title", "");
            anime.url = result.getString("url", "");
            anime.rank = result.getInt("rank", Integer.MAX_VALUE);
            anime.type = result.getString("type", "");
            anime.trailer = result.getObject("trailer").getString("url", "Unkown");
            anime.episodeCount = result.getInt("episodes", 0);
            animes.add(anime);
        }
        return animes;
    }

    public static Anime parseAnime(DataObject data) {
        Anime anime = new Anime();
        DataObject result = null;
        try {
            result = data.getObject("data");
        } catch(Exception e) { return null; }
        anime.art = result.getObject("images").getObject("jpg").getString("image_url");
        anime.malID = result.getInt("mal_id", 0);
        anime.status = result.getString("status", "");
        anime.rating = result.getString("rating", "");
        anime.score = result.getDouble("score", 0);
        anime.summary = result.getString("synopsis", "");
        anime.title = result.getString("title", "");
        anime.url = result.getString("url", "");
        anime.rank = result.getInt("rank", Integer.MAX_VALUE);
        anime.type = result.getString("type", "");
        anime.trailer = result.getObject("trailer").getString("url", "Unkown");
        anime.episodeCount = result.getInt("episodes", 0);
        return anime;
    }

    public static ArrayList<Article> getNews(DataObject data) {
        ArrayList<Article> articles = new ArrayList<>();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            Article a = new Article();
            a.author = res.getString("author_username", "");
            a.authorUrl = res.getString("author_url", "");
            a.url = res.getString("url", "");
            a.title = res.getString("title", "");
            a.comments = res.getInt("comments", 0);
            a.date = res.getString("date", "");
            a.desc = res.getString("excerpt", "");
            a.forum = res.getString("forum_url", "");
            a.image = res.getObject("images").getObject("jpg").getString("image_url", "");
            articles.add(a);
        }
        return articles;
    }

    public static Statistic parseStats(DataObject data) {
        Statistic stat = new Statistic();
        DataObject result = null;
        try {
            result = data.getObject("data");
        } catch(Exception e) { return null; }
        stat.watching = result.getLong("watching", 0);
        stat.completed = result.getLong("completed", 0);
        stat.onHold = result.getLong("on_hold", 0);
        stat.dropped = result.getLong("dropped", 0);
        stat.planToWatch = result.getLong("plan_to_watch", 0);
        stat.total = result.getLong("total", 0);
        return stat;
    }

}
