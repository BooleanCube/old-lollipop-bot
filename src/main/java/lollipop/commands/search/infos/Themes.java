package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.IOException;

public class Themes {

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        API api = new API();
        long id = page.animes.get(page.pageNumber-1).malID;
        try {
            MessageEmbed themeEmbed = api.getAnimeThemes(id).build();
            event.replyEmbeds(themeEmbed).setEphemeral(true).queue();
            page.themes.put(page.pageNumber, themeEmbed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
