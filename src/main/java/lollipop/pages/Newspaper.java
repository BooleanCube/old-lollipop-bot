package lollipop.pages;

import awatch.model.Article;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

/**
 * Newspaper Model for news command
 */
public class Newspaper {

    public ArrayList<Article> articles = new ArrayList<>();
    public int pageNumber = 0;
    public Message msg = null;
    public User user;

    /**
     * Newspaper Constructor to initialize variables
     * @param a article list
     * @param pn page number
     * @param m message
     * @param u user
     */
    public Newspaper(ArrayList<Article> a, int pn, Message m, User u) {
        articles = a;
        pageNumber = pn;
        msg = m;
        user = u;
    }

}