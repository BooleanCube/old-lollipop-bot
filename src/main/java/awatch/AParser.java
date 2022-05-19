package awatch;

import awatch.models.*;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
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

    public static ArrayList<Anime> parseFirst(DataObject data) {
        ArrayList<Anime> animes = new ArrayList<>();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        int size = Math.min(25, arr.length());
        for(int i=0; i<size; i++) {
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

    public static ArrayList<Episode> parseEpisodes(DataObject data) {
        ArrayList<Episode> episodes = new ArrayList<>();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            Episode e = new Episode();
            e.url = res.getString("url", "");
            e.title = res.getString("title_romanji", res.getString("title", ""));
            episodes.add(e);
        }
        return episodes;
    }

    public static EmbedBuilder parseReview(DataObject data) {
        Review r = new Review();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        DataObject res = arr.getObject(0);
        r.authorName = res.getObject("user").getString("username", "Unkown Name");
        r.authorIcon = res.getObject("user").getObject("images").getObject("jpg").getString("image_url", "https://api-private.atlassian.com/users/63729d1b358a0c5f1c38cf368ad9d693/avatar");
        r.authorUrl = res.getObject("user").getString("url", "https://myanimelist.net/reviews.php");
        r.url = res.getString("url", "https://myanimelist.net/reviews.php");
        r.details = res.getString("review", "Empty Review Description");
        r.votes = res.getInt("votes", 0);
        r.score = res.getObject("scores").getInt("overall", 0);
        return r.getEmbed();
    }

    public static String parseRecommendation(DataObject data) {
        StringBuilder animes = new StringBuilder();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
            int size = Math.min(10, arr.length());
            for(int i=0; i<size; i++) {
                DataObject res = arr.getObject(i);
                String title = res.getObject("entry").getString("title", "Title");
                String url = res.getObject("entry").getString("url", "https://myanimelist.net/");
                animes.append("#").append(i+1).append(" - [").append(title).append("](").append(url).append(")\n");
            }
        } catch(Exception e) { return null; }
        return animes.toString();
    }

    public static EmbedBuilder parseThemes(DataObject data) {
        StringBuilder op = new StringBuilder();
        StringBuilder end = new StringBuilder();
        DataObject res = null;
        try {
            res = data.getObject("data");
            DataArray arr = null;
            arr = res.getArray("openings");
            for(int i=0; i<arr.length(); i++) {
                String result = arr.getString(i, "");
                op.append(result).append("\n");
            }
            arr = res.getArray("endings");
            for(int i=0; i<arr.length(); i++) {
                String result = arr.getString(i, "");
                end.append(result).append("\n");
            }
        } catch(Exception e) { return null; }
        return Tools.themesToEmbed(op.toString(), end.toString());
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
