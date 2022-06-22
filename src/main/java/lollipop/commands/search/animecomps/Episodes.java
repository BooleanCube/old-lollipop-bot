package lollipop.commands.search.animecomps;

import lollipop.API;
import lollipop.pages.AnimePage;
import lollipop.pages.EpisodeList;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.HashMap;

/**
 * Episode Model for search command
 */
public class Episodes {

    static API api = new API();
    public static HashMap<Long, EpisodeList> messageToPage = new HashMap<>();

    /**
     * Runs a request to get anime episodes
     * @param event select menu interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getEpisodes(event, page);
    }

}
