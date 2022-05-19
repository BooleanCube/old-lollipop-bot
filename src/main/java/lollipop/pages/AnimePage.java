package lollipop.pages;

import awatch.models.Anime;
import mread.model.Manga;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimePage {
    public ArrayList<Anime> animes = null;
    public ArrayList<Manga> mangas = null;
    public Message msg;
    public int pageNumber;
    public User user;

    public HashMap<Integer, MessageEmbed> stats = new HashMap<>();
    public HashMap<Integer, Newspaper> news = new HashMap<>();
    public HashMap<Integer, String> picture = new HashMap<>();
    public HashMap<Integer, EpisodeList> episodes = new HashMap<>();
    public HashMap<Integer, MessageEmbed> themes = new HashMap<>();
    public HashMap<Integer, MessageEmbed> review = new HashMap<>();
    public HashMap<Integer, MessageEmbed> recommendations = new HashMap<>();

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
