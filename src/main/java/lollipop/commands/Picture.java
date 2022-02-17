package lollipop.commands;

import lollipop.API;
import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Picture implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"picture", "pic"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Gives you a random picture related to the query!\n*Note: You must pass in the `ID` instead of a name. The `ID` can be located at the top of a `search` command.*\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [anime/character] [ID]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOptions(new OptionData(OptionType.STRING, "type", "anime / manga / character", true)
                        .addChoice("anime", "anime")
                        .addChoice("character", "character"))
                .addOption(OptionType.INTEGER, "id", "MAL ID (available in the search command)", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        if(args.size() != 2) { Tools.wrongUsage(event, this); return; }
        if(args.get(0).equalsIgnoreCase("anime") || args.get(0).equalsIgnoreCase("a")) {
            API api = new API();
            long id = options.get(1).getAsLong();
            InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for pictures...").build()).complete();
            try {
                msg.editOriginalEmbeds(
                        Tools.pictureEmbed(
                                api.pictureAnime(id))
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
        } else if(args.get(0).equalsIgnoreCase("manga") || args.get(0).equalsIgnoreCase("m")) {
            API api = new API();
            long id = options.get(1).getAsLong();
            InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for pictures...").build()).complete();
            try {
                msg.editOriginalEmbeds(
                        Tools.pictureEmbed(
                                 api.pictureManga(id))
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
        } else if(args.get(0).equalsIgnoreCase("character") || args.get(0).equalsIgnoreCase("c")) {
            API api = new API();
            long id = options.get(1).getAsLong();
            InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for pictures...").build()).complete();
            try {
                msg.editOriginalEmbeds(
                        Tools.pictureEmbed(
                                api.pictureCharacter(id))
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
        } else Tools.wrongUsage(event, this);
    }
}
