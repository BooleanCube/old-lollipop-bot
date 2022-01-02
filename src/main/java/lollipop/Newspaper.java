package lollipop;

import awatch.models.Article;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Newspaper {
    public ArrayList<Article> articles = new ArrayList<>();
    public int pageNumber = 0;
    public Message msg = null;
    public User user;
    public Newspaper(ArrayList<Article> a, int pn, Message m, User u) {
        articles = a;
        pageNumber = pn;
        msg = m;
        user = u;
    }
}