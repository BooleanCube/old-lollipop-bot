package lollipop.commands;

import lollipop.API;
import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SearchAnime implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"searchanime", "searcha", "sa"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Searches for an anime with the given search query!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [query]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "query", "search anime name", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        API api = new API();
        try {
            String query = String.join(" ", args);
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
            ScheduledFuture timeout = msg.editMessageEmbeds(new EmbedBuilder()
                    .setColor(Color.red)
                    .setDescription("Could not find an anime with that search query! Please try again with a valid anime!")
                    .build()
            ).queueAfter(5, TimeUnit.SECONDS);
            msg.editMessageEmbeds(Tools.animeToEmbed(api.searchForAnime(query).get(0)).build()).queue();
            timeout.cancel(true);
        }
        catch (IOException e) {}
    }
}

