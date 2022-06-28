package lollipop.commands;

import lollipop.*;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Random implements Command {

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
        return Tools.defaultSlashCmd(this).addOptions(
                new OptionData(OptionType.STRING, "type", "anime / manga / character", true)
                        .addChoice("anime", "anime")
                        .addChoice("manga", "manga")
                        .addChoice("character", "character")
        );
    }

    static API api = new API();
    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final java.util.List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        boolean nsfw = false;
        if(event.getChannel().getType() == ChannelType.TEXT) nsfw = event.getTextChannel().isNSFW();
        switch (args.get(0)) {
            case "anime" -> {
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
                api.randomAnime(interactionHook, nsfw);
                break;
            }
            case "manga" -> {
                InteractionHook interactionHook = event.replyEmbeds(
                        new EmbedBuilder()
                                .setDescription("Retrieving a `random manga`...")
                                .build()
                ).complete();
                api.randomManga(interactionHook, nsfw);
                break;
            }
            case "character" -> {
                InteractionHook interactionHook = event.replyEmbeds(
                        new EmbedBuilder()
                                .setDescription("Retrieving a `random character`...")
                                .build()
                ).complete();
                api.randomCharacter(interactionHook, nsfw);
                break;
            }
        }
    }

}
