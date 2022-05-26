package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.IOException;

/**
 * Reviews Model for search command
 */
public class Reviews {

    static API api = new API();

    /**
     * Runs a request to get anime top review
     * @param event button interaction event
     * @param page anime page
     */
    public static void run(ButtonInteractionEvent event, AnimePage page) {
        api.getReview(event, page);
    }

}
