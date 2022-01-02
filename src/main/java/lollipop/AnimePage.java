package lollipop;

import awatch.models.Anime;
import mread.model.Manga;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class AnimePage {
    public ArrayList<Anime> animes = null;
    public ArrayList<Manga> mangas = null;
    public Message msg;
    public int pageNumber;
    public User user;
    public AnimePage(ArrayList<Anime> as, Message m, int pn, User u) {
        animes = as;
        msg = m;
        pageNumber = pn;
        user = u;
    }
    public AnimePage(ArrayList<Manga> ms, int pn, Message m, User u) {
        mangas = ms;
        msg = m;
        pageNumber = pn;
        user = u;
    }
    public AnimePage(Anime a, Message m, User u) {
        animes = new ArrayList<>();
        animes.add(a);
        msg = m;
        user = u;
    }
}
