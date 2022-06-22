package lollipop.commands.search.animecomps;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

/**
 * Statistics Model for search command
 */
public class Statistics {

    static API api = new API();

    /**
     * Runs a request to get anime statistics
     * @param event select menu interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getStatistics(event, page);
    }

}
