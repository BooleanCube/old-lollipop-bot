package lollipop.commands;

import awatch.model.Anime;
import lollipop.API;
import lollipop.Command;
import lollipop.Constant;
import lollipop.Tools;
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

public class Latest implements Command {

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public String[] getAliases() {
        return new String[]{"latest"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Retrieves the latest animes released in the current season of the year!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    static API api = new API();

    @Override
    public void run(SlashCommandInteractionEvent event) {
        InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Getting the `Latest` anime of the season...").build()).complete();
        Message message = msg.retrieveOriginal().complete();
        ScheduledFuture<?> timeout = msg.editOriginalEmbeds(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("No recently released animes were found for this season! Try again later!")
                        .build()
        ).queueAfter(5, TimeUnit.SECONDS, me -> messageToPage.remove(message.getIdLong()));
        messageToPage.put(message.getIdLong(), new AnimePage(null, message, 1, event.getUser(), timeout));
        api.getLatest(msg);
    }

}
