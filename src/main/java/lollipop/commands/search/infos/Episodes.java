package lollipop.commands.search.infos;

import lollipop.API;
import lollipop.Tools;
import lollipop.pages.AnimePage;
import lollipop.pages.EpisodeList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Episodes {

    static API api = new API();
    public static HashMap<Long, EpisodeList> messageToPage = new HashMap<>();

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        InteractionHook msg = event.replyEmbeds(
                new EmbedBuilder()
                        .setDescription("Searching for episodes...")
                        .build()
        ).setEphemeral(true).complete();
        api.getEpisodes(msg.retrieveOriginal().complete(), id);
        page.episodes.put(page.pageNumber, new EpisodeList(null, 0, msg.retrieveOriginal().complete(), event.getUser()));
    }

}
