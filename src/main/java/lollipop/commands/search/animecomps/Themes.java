package lollipop.commands.search.animecomps;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

/**
 * Runs a request to get anime themes
 */
public class Themes {

    static API api = new API();

    /**
     * Runs a request to get anime themes
     * @param event select menu interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getThemes(event, page);
    }

}
