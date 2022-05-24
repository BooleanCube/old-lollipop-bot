package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.IOException;

public class Themes {

    static API api = new API();

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        api.getThemes(event, page);
    }

}
