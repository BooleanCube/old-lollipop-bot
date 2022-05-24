package lollipop.commands;

import awatch.model.Anime;
import lollipop.*;
import lollipop.pages.AnimePage;
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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Top implements Command {

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

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
        return "Retrieves the top 25 anime in the world!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        API api = new API();
        InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Getting the `Top 25` anime...").build()).complete();
        Message message = msg.retrieveOriginal().complete();
        ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                .setColor(Color.red)
                .setDescription("Could not retreive the top 25 animes! Please try again later!")
                .build()
        ).queueAfter(5, TimeUnit.SECONDS, me -> messageToPage.remove(message.getIdLong()));
        messageToPage.put(message.getIdLong(), new AnimePage(null, message, 1, event.getUser(), timeout));
        api.getTop(msg);
    }
}
