package lollipop.pages;

import awatch.model.Character;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

public class CharacterPage {

    public ArrayList<Character> characters = null;
    public Message msg;
    public int pageNumber;
    public User user;
    public ScheduledFuture<?> timeout;
    public String currentPlaceholder = "Sort by popularity";

    /**
     * Anime List Constructor
     * @param cs characters
     * @param m message
     * @param pn page number
     * @param u user
     * @param t timeout
     */
    public CharacterPage(ArrayList<Character> cs, Message m, int pn, User u, ScheduledFuture<?> t) {
        characters = cs;
        msg = m;
        pageNumber = pn;
        user = u;
        timeout = t;
    }

    /**
     * Singular Anime Constructor
     * @param c character
     * @param m message
     * @param u user
     */
    public CharacterPage(Character c, Message m, User u) {
        characters = new ArrayList<>();
        characters.add(c);
        msg = m;
        user = u;
    }

    /**
     * Constructor for starting search command with timeout
     * @param m message
     * @param u user
     * @param t timeout
     */
    public CharacterPage(Message m, User u, ScheduledFuture<?> t) {
        characters = new ArrayList<>();
        msg = m;
        user = u;
        timeout = t;
    }

}
