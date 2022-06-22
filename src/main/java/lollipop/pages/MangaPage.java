package lollipop.pages;

import mread.model.Manga;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

public class MangaPage {

    public ArrayList<Manga> mangas = null;
    public Message msg;
    public int pageNumber;
    public User user;
    public ScheduledFuture<?> timeout;
    public String currentPlaceholder = "Sort by popularity";

    /**
     * Anime List Constructor
     * @param ms mangas
     * @param m message
     * @param pn page number
     * @param u user
     * @param t timeout
     */
    public MangaPage(ArrayList<Manga> ms, Message m, int pn, User u, ScheduledFuture<?> t) {
        mangas = ms;
        msg = m;
        pageNumber = pn;
        user = u;
        timeout = t;
    }

    /**
     * Singular Anime Constructor
     * @param ma manga
     * @param me message
     * @param u user
     */
    public MangaPage(Manga ma, Message me, User u) {
        mangas = new ArrayList<>();
        mangas.add(ma);
        msg = me;
        user = u;
    }

    /**
     * Constructor for starting search command with timeout
     * @param m message
     * @param u user
     * @param t timeout
     */
    public MangaPage(Message m, User u, ScheduledFuture<?> t) {
        mangas = new ArrayList<>();
        msg = m;
        user = u;
        timeout = t;
    }

}
