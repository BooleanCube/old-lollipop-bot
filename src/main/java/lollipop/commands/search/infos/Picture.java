package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.Tools;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.io.IOException;

public class Picture {

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        API api = new API();
        long id = page.animes.get(page.pageNumber-1).malID;
        InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for pictures...").build()).setEphemeral(true).complete();
        try {
            String url = api.pictureAnime(id);
            page.picture.put(page.pageNumber, url);
            msg.editOriginalEmbeds(
                    Tools.pictureEmbed(url)
                            .setAuthor(event.getMember().getEffectiveName(), event.getMember().getAvatarUrl(), event.getMember().getEffectiveAvatarUrl())
                            .build()
            ).queue();
        }
        catch (IOException e) {
            msg.editOriginalEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any pictures related to that ID! Check for any typos.")
                            .build()
            ).queue();
        }
    }
}
