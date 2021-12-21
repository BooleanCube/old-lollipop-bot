package lollipop.commands;

import lollipop.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Search implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"search", "s"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Searches for an anime/manga/charcater with the given search query!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [anime/manga/character] [query]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.size()<2) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        if(args.get(0).equalsIgnoreCase("c") || args.get(0).equalsIgnoreCase("character")) {
            AnimeAPI api = new AnimeAPI();
            String query = String.join(" ", args.subList(1, args.size()));
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
            try {
                msg.editMessageEmbeds(Tools.characterToEmbed(api.searchForCharacter(query)).build()).queue();
            }
            catch (IOException e) {
                msg.editMessageEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Could not find a character with that search query! Please try again with a valid character!").build()).queue();
            }
        }
        else if(args.get(0).equalsIgnoreCase("a") || args.get(0).equalsIgnoreCase("anime")) {
            AnimeAPI api = new AnimeAPI();
            try {
                String query = String.join(" ", args.subList(1, args.size()));
                Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
                ScheduledFuture timeout = msg.editMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Could not find an anime with that search query! Please try again with a valid anime!")
                        .build()
                ).queueAfter(5, TimeUnit.SECONDS);
                msg.editMessageEmbeds(Tools.animeToEmbed(api.searchForAnime(query)).build()).queue();
                timeout.cancel(true);
            }
            catch (IOException e) {}
        }
        else if(args.get(0).equalsIgnoreCase("m") || args.get(0).equalsIgnoreCase("manga")) {
            MangaAPI api = new MangaAPI();
            String query = String.join(" ", args.subList(1, args.size()));
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
            api.searchMangas(query, msg);
        }
        else Tools.wrongUsage(event.getTextChannel(), this);
    }
}
