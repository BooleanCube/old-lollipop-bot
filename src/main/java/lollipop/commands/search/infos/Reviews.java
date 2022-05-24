package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.IOException;

public class Reviews {

    static API api = new API();

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        api.getReview(event, page);
    }

}
