package lollipop.commands;

import lollipop.API;
import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;

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
        return "Gives you a random picture related to the query!\n*Note: You must pass in the `ID` instead of a name. The `ID` can be located at the top of a `search` command.*\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [anime/character] [ID]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.size() != 2) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        if(args.get(0).equalsIgnoreCase("anime") || args.get(0).equalsIgnoreCase("a")) {
            API api = new API();
            long id = 0;
            try { id = Long.parseLong(args.get(1)); }
            catch(Exception e) { Tools.wrongUsage(event.getTextChannel(), this); return; }
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for pictures...").build()).complete();
            try {
                msg.editMessageEmbeds(
                        Tools.pictureEmbed(
                                api.pictureAnime(id))
                                        .setAuthor(event.getMember().getEffectiveName(), event.getMember().getAvatarUrl(), event.getMember().getEffectiveAvatarUrl())
                                        .build()
                ).queue();
            }
            catch (IOException e) {
                msg.editMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.red)
                                .setDescription("Could not find any pictures related to that ID! Check for any typos.")
                                .build()
                ).queue();
            }
        } else if(args.get(0).equalsIgnoreCase("manga") || args.get(0).equalsIgnoreCase("m")) {
            API api = new API();
            long id = 0;
            try { id = Long.parseLong(args.get(1)); }
            catch(Exception e) { Tools.wrongUsage(event.getTextChannel(), this); return; }
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for pictures...").build()).complete();
            try {
                msg.editMessageEmbeds(
                        Tools.pictureEmbed(
                                 api.pictureManga(id))
                                        .setAuthor(event.getMember().getEffectiveName(), event.getMember().getAvatarUrl(), event.getMember().getEffectiveAvatarUrl())
                                        .build()
                ).queue();
            }
            catch (IOException e) {
                msg.editMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.red)
                                .setDescription("Could not find any pictures related to that ID! Check for any typos.")
                                .build()
                ).queue();
            }
        } else if(args.get(0).equalsIgnoreCase("character") || args.get(0).equalsIgnoreCase("c")) {
            API api = new API();
            long id = 0;
            try { id = Long.parseLong(args.get(1)); }
            catch(Exception e) { Tools.wrongUsage(event.getTextChannel(), this); return; }
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for pictures...").build()).complete();
            try {
                msg.editMessageEmbeds(
                        Tools.pictureEmbed(
                                api.pictureCharacter(id))
                                        .setAuthor(event.getMember().getEffectiveName(), event.getMember().getAvatarUrl(), event.getMember().getEffectiveAvatarUrl())
                                        .build()
                ).queue();
            }
            catch (IOException e) {
                msg.editMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.red)
                                .setDescription("Could not find any pictures related to that ID! Check for any typos.")
                                .build()
                ).queue();
            }
        } else Tools.wrongUsage(event.getTextChannel(), this);
    }
}
