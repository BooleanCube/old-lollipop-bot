package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.IOException;

public class Reviews {

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        API api = new API();
        long id = page.animes.get(page.pageNumber-1).malID;
        try {
            MessageEmbed reviewEmbed = api.getAnimeReview(id).build();
            event.replyEmbeds(reviewEmbed).setEphemeral(true).queue();
            page.review.put(page.pageNumber, reviewEmbed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
