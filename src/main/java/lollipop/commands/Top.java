package lollipop.commands;

import awatch.models.Anime;
import lollipop.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Top implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"top"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Gets the top 10 anime in the world!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + "`";
    }

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        AnimeAPI api = new AnimeAPI();
        try {
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Getting the `Top 10` anime...").build()).complete();
            ScheduledFuture<?> timeout = msg.editMessageEmbeds(new EmbedBuilder()
                    .setColor(Color.red)
                    .setDescription("Could not find an anime with that search query! Please try again with a valid anime!")
                    .build()
            ).queueAfter(5, TimeUnit.SECONDS);
            ArrayList<Anime> animes = api.topAnime();
            if(animes == null) throw new IOException();
            Message m = msg.editMessageEmbeds(Tools.animeToEmbed(animes.get(0)).setFooter("Page 1/" + animes.size()).build()).setActionRow(
                    net.dv8tion.jda.api.interactions.components.Button.secondary("left", Emoji.fromUnicode("⬅")),
                    net.dv8tion.jda.api.interactions.components.Button.secondary("right", Emoji.fromUnicode("➡")),
                    Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer")
            ).complete();
            messageToPage.put(m.getIdLong(), new AnimePage(animes, m, 1, event.getAuthor()));
            timeout.cancel(true);
            m.editMessageComponents().queueAfter(3, TimeUnit.MINUTES, me -> messageToPage.remove(m.getIdLong()));
        }
        catch(IOException ignored) {}
    }
}
