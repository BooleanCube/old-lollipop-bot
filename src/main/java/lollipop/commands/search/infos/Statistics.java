package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.Tools;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.IOException;

/**
 * Statistics Model for search command
 */
public class Statistics {

    static API api = new API();

    /**
     * Runs a request to get anime statistics
     * @param event button interaction event
     * @param page anime page
     */
    public static void run(ButtonInteractionEvent event, AnimePage page) {
        api.getStatistics(event, page);
    }

}
