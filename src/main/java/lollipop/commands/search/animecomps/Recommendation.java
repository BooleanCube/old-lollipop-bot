package lollipop.commands.search.animecomps;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

/**
 * Recommendation Model for search command
 */
public class Recommendation {

    static API api = new API();

    /**
     * Runs a reqest to get anime recommendations
     * @param event select menu interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getRecommendation(event, page);
    }

}
