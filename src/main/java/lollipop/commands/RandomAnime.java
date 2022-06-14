package lollipop.commands;

import awatch.model.Anime;
import lollipop.*;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RandomAnime implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"random", "r"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Get a random anime!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    static API api = new API();
    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public void run(SlashCommandInteractionEvent event) {
        InteractionHook interactionHook = event.replyEmbeds(
                new EmbedBuilder()
                        .setDescription("Retrieving a `random anime`...")
                        .build()
        ).complete();
        Message message = interactionHook.retrieveOriginal().complete();
        ScheduledFuture<?> timeout = interactionHook.editOriginalEmbeds(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription("Could not successfully retrieve a random anime! Try again later!")
                        .build()
        ).queueAfter(5, TimeUnit.SECONDS, me -> messageToPage.remove(message.getIdLong()));
        messageToPage.put(message.getIdLong(), new AnimePage(message, event.getUser(), timeout));
        boolean nsfw = false;
        if(event.getChannel().getType() == ChannelType.TEXT) nsfw = event.getTextChannel().isNSFW();
        api.randomAnime(interactionHook, nsfw);
    }

}
