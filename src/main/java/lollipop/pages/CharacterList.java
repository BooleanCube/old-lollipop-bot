package lollipop.pages;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class CharacterList {

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
    public CharacterList(ArrayList<StringBuilder> p, int pn, Message m, User u) {
        pages = p;
        pageNumber = pn;
        msg = m;
        user = u;
    }

}
