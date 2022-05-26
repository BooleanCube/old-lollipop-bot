package lollipop.pages;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

/**
 * Episode List model to keep track of all the episodes of a certain anime
 */
public class EpisodeList {
    public ArrayList<StringBuilder> pages;
    public int pageNumber = 0;
    public Message msg = null;
    public User user;

    /**
     * Constructor initializes variables
     * @param p pages
     * @param pn page number
     * @param m message
     * @param u user
     */
    public EpisodeList(ArrayList<StringBuilder> p, int pn, Message m, User u) {
        pages = p;
        pageNumber = pn;
        msg = m;
        user = u;
    }

}