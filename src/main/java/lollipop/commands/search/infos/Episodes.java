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

    public static HashMap<Long, EpisodeList> messageToPage = new HashMap<>();

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        API api = new API();
        long id = page.animes.get(page.pageNumber-1).malID;
        InteractionHook msg = event.replyEmbeds(
                new EmbedBuilder()
                        .setDescription("Searching for episodes...")
                        .build()
        ).setEphemeral(true).complete();
        try {
            ArrayList<StringBuilder> pageContent = Tools.episodeEmbeds(api.animeEpisodes(page.animes.get(page.pageNumber-1).malID));
            Message m = msg.editOriginalEmbeds(
                    new EmbedBuilder()
                            .setTitle("Episode List")
                            .setDescription(pageContent.get(0))
                            .setFooter("Page 1/" + pageContent.size())
                            .build()
            ).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            page.episodes.put(page.pageNumber, new EpisodeList(pageContent, 0, m, event.getUser()));
            messageToPage.put(m.getIdLong(), page.episodes.get(page.pageNumber));
            m.editMessageComponents()
                    .queueAfter(3, TimeUnit.MINUTES, me -> messageToPage.remove(m.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            msg.editOriginalEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any episodes from that anime! Please try again later!")
                            .build()
            ).queue();
        }
    }

}
