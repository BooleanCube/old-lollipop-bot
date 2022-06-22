package lollipop.commands.search.mangacomps;

import lollipop.API;
import lollipop.pages.ChapterList;
import lollipop.pages.MangaPage;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.HashMap;

public class Chapters {

    static API api = new API();
    public static HashMap<Long, ChapterList> messageToPage = new HashMap<>();

    /**
     * Start the Chapter List component
     * @param event select menu interaction
     * @param page manga page
     */
    public static void run(SelectMenuInteractionEvent event, MangaPage page) {
        api.getChapters(event, page);
    }

}
