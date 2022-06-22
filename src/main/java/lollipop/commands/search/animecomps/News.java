package lollipop.commands.search.animecomps;

import lollipop.*;
import lollipop.pages.AnimePage;
import lollipop.pages.Newspaper;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;


import java.util.HashMap;

/**
 * News Model for search command
 */
public class News {

    static API api = new API();
    public static HashMap<Long, Newspaper> messageToPage = new HashMap<>();

    /**
     * Runs a request to get anime news
     * @param event select menu interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getNews(event, page);
    }

}