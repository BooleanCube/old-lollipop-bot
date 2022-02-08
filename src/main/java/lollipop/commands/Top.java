package lollipop.commands;

import awatch.models.Anime;
import lollipop.*;
import lollipop.models.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
        return "Gets the top 10 anime in the world!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public void run(SlashCommandInteractionEvent event) {
        API api = new API();
        try {
            InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Getting the `Top 10` anime...").build()).complete();
            ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                    .setColor(Color.red)
                    .setDescription("Could not retreive the top 10 animes! Please try again later!")
                    .build()
            ).queueAfter(5, TimeUnit.SECONDS);
            ArrayList<Anime> animes = api.topAnime();
            if(animes == null) throw new IOException();
            Message m = msg.editOriginalEmbeds(Tools.animeToEmbed(animes.get(0)).setFooter("Page 1/" + animes.size()).build()).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡")),
                    Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer")
            ).complete();
            messageToPage.put(m.getIdLong(), new AnimePage(animes, m, 1, event.getUser()));
            timeout.cancel(true);
            m.editMessageComponents()
                    .setActionRow(
                            Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                            Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled(),
                            Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled()
                    )
                    .queueAfter(3, TimeUnit.MINUTES, me -> messageToPage.remove(m.getIdLong()));
        }
        catch(IOException ignored) {}
    }
}
