package lollipop.pages;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class EpisodeList {
    public ArrayList<StringBuilder> pages = new ArrayList<>();
    public int pageNumber = 0;
    public Message msg = null;
    public User user;
    public EpisodeList(ArrayList<StringBuilder> p, int pn, Message m, User u) {
        pages = p;
        pageNumber = pn;
        msg = m;
        user = u;
    }
}