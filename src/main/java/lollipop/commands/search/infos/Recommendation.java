package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.Tools;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.io.IOException;

/**
 * Recommendation Model for search command
 */
public class Recommendation {

    static API api = new API();

    /**
     * Runs a reqest to get anime recommendations
     * @param event button interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getRecommendation(event, page);
    }

}
