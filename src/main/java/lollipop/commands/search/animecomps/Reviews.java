package lollipop.commands.search.animecomps;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

/**
 * Reviews Model for search command
 */
public class Reviews {

    static API api = new API();

    /**
     * Runs a request to get anime top review
     * @param event select menu interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getReview(event, page);
    }

}
