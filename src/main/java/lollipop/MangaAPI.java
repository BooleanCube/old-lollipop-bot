package lollipop;

import mread.controller.RClient;
import mread.controller.RListener;
import mread.model.Chapter;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

//api path: https://www.readm.org/

public class MangaAPI implements RListener {

    public static String apiPath = "https://www.readm.org";
    private ArrayList<Manga> mangaSearched = new ArrayList<>();
    RClient api = new RClient(this);
    private Message messageToEdit = null;

    public void searchMangas(String query, Message c) {
        api.search(query);
        messageToEdit = c;
    }

    @Override
    public void setMangas(List<Manga> mangas) {
        if(mangas.isEmpty()) { messageToEdit.editMessageEmbeds(new EmbedBuilder().setDescription("Could not find any results with that search query! Please try again with a valid manga!").build()).queue(); return; }
        for(Manga manga : mangas) mangaSearched.add(manga);
        messageToEdit.editMessageEmbeds(Tools.mangaToEmbed(mangas.get(0)).build()).queue();
    }

    @Override
    public void setChapters(Manga manga) {
        for (Chapter c : manga.chapters) {
            System.out.println(c);
        }
        api.pages(manga.chapters.get(0));
    }

    @Override
    public void setPages(Chapter chapter) {
        for (String s : chapter.pages) {
            System.out.println(s);
        }
    }

}
