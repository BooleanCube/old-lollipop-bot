package mread.controller;

import awatch.model.Article;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.ArrayList;

public class RParser {

    //unused and not updated to the new v4 jikan api
    public static ArrayList<Article> getNews(DataObject data) {
        ArrayList<Article> articles = new ArrayList<>();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            Article a = new Article();
            a.author = res.getString("author_name", "Unkown");
            a.authorUrl = res.getString("author_url", "");
            a.url = res.getString("url");
            a.title = res.getString("title", "");
            a.comments = res.getInt("comments", 0);
            a.date = res.getString("date", "Unknown");
            a.desc = res.getString("intro", "");
            a.forum = res.getString("forum_url", "");
            a.image = res.getString("image_url", "");
            articles.add(a);
        }
        return articles;
    }

}
