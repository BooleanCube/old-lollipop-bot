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

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

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
                ScheduledFuture<?> timeout = msg.editMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Could not find an anime with that search query! Please try again with a valid anime!")
                        .build()
                ).queueAfter(5, TimeUnit.SECONDS);
                ArrayList<Anime> animes = api.searchForAnime(query);
                if(animes == null) throw new IOException();
                Message m = msg.editMessageEmbeds(Tools.animeToEmbed(animes.get(0)).setFooter("Page 1/" + animes.size()).build()).setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡")),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer")
                ).complete();
                messageToPage.put(m.getIdLong(), new AnimePage(animes, m, 1, event.getAuthor()));
                timeout.cancel(true);
                m.editMessageComponents().queueAfter(3, TimeUnit.MINUTES, me -> messageToPage.remove(m.getIdLong()));
            }
            catch(IOException ignored) {}
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
