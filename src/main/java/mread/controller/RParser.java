package mread.controller;

import awatch.models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RParser {

    //unused and not updated to the new v4 jikan api
    public static ArrayList<Article> getNews(JSONObject data) {
        ArrayList<Article> articles = new ArrayList<>();
        JSONArray arr = null;
        try {
            arr = data.getJSONArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            JSONObject res = arr.getJSONObject(i);
            Article a = new Article();
            try { a.author = res.getString("author_name"); } catch(Exception ignored) {}
            try { a.authorUrl = res.getString("author_url"); } catch(Exception ignored) {}
            try { a.url = res.getString("url"); } catch(Exception ignored) {}
            try { a.title = res.getString("title"); } catch(Exception ignored) {}
            try { a.comments = res.getInt("comments"); } catch(Exception ignored) {}
            try { a.date = res.getString("date"); } catch(Exception ignored) {}
            try { a.desc = res.getString("intro"); } catch(Exception ignored) {}
            try { a.forum = res.getString("forum_url"); } catch(Exception ignored) {}
            try { a.image = res.getString("image_url"); } catch(Exception ignored) {}
            articles.add(a);
        }
        return articles;
    }

}
