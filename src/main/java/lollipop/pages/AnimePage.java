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
    public ArrayList<Manga> mangas = null;
    public Message msg;
    public int pageNumber;
    public User user;
    public ScheduledFuture<?> timeout;

    public HashMap<Integer, MessageEmbed> stats = new HashMap<>();
    public HashMap<Integer, Newspaper> news = new HashMap<>();
    public HashMap<Integer, String> picture = new HashMap<>();
    public HashMap<Integer, EpisodeList> episodes = new HashMap<>();
    public HashMap<Integer, MessageEmbed> themes = new HashMap<>();
    public HashMap<Integer, MessageEmbed> review = new HashMap<>();
    public HashMap<Integer, MessageEmbed> recommendations = new HashMap<>();

    /**
     * Anime List Constructor
     * @param as
     * @param m
     * @param pn
     * @param u
     * @param t
     */
    public AnimePage(ArrayList<Anime> as, Message m, int pn, User u, ScheduledFuture<?> t) {
        animes = as;
        msg = m;
        pageNumber = pn;
        user = u;
        timeout = t;
    }

    /**
     * Manga List Constructor
     * @param ms
     * @param pn
     * @param m
     * @param u
     */
    public AnimePage(ArrayList<Manga> ms, int pn, Message m, User u) {
        mangas = ms;
        msg = m;
        pageNumber = pn;
        user = u;
    }

    /**
     * Singular Anime Constructor
     * @param a
     * @param m
     * @param u
     */
    public AnimePage(Anime a, Message m, User u) {
        animes = new ArrayList<>();
        animes.add(a);
        msg = m;
        user = u;
    }

    /**
     * Constructor for starting search command with timeout
     * @param m
     * @param u
     * @param t
     */
    public AnimePage(Message m, User u, ScheduledFuture<?> t) {
        animes = new ArrayList<>();
        msg = m;
        user = u;
        timeout = t;
    }

}
