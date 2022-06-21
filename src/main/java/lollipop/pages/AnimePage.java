package lollipop.pages;

import awatch.model.Anime;
import mread.model.Manga;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Anime Page Model represents a singular page in pagination commands
 */
public class AnimePage {

    public ArrayList<Anime> animes = null;
    public Message msg;
    public int pageNumber;
    public User user;
    public ScheduledFuture<?> timeout;
    public String currentPlaceholder = "Sort by popularity";

    /**
     * Anime List Constructor
     * @param as animes
     * @param m message
     * @param pn page number
     * @param u user
     * @param t timeout
     */
    public AnimePage(ArrayList<Anime> as, Message m, int pn, User u, ScheduledFuture<?> t) {
        animes = as;
        msg = m;
        pageNumber = pn;
        user = u;
        timeout = t;
    }

    /**
     * Singular Anime Constructor
     * @param a anime
     * @param m message
     * @param u user
     */
    public AnimePage(Anime a, Message m, User u) {
        animes = new ArrayList<>();
        animes.add(a);
        msg = m;
        user = u;
    }

    /**
     * Constructor for starting search command with timeout
     * @param m message
     * @param u user
     * @param t timeout
     */
    public AnimePage(Message m, User u, ScheduledFuture<?> t) {
        animes = new ArrayList<>();
        msg = m;
        user = u;
        timeout = t;
    }

}
