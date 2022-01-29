package lollipop.commands;

import awatch.models.Anime;
import lollipop.*;
import lollipop.models.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
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
        return "Searches for an anime/manga/charcater with the given search query!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [anime/manga/character] [query]`";
    }

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.STRING, "type", "(a)nime / (m)anga / (c)haracter", true)
                .addOption(OptionType.STRING, "query", "search query", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.size()<2) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        API api = new API();
        if(args.get(0).equalsIgnoreCase("c") || args.get(0).equalsIgnoreCase("character")) {
            String query = String.join(" ", args.subList(1, args.size()));
            InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
            try {
                msg.editOriginalEmbeds(Tools.characterToEmbed(api.searchForCharacter(query)).build()).queue();
            }
            catch (IOException e) {
                msg.editOriginalEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Could not find a character with that search query! Please try again with a valid character!").build()).queue();
            }
        }
        else if(args.get(0).equalsIgnoreCase("a") || args.get(0).equalsIgnoreCase("anime")) {
            try {
                String query = String.join(" ", args.subList(1, args.size()));
                InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
                ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Could not find an anime with that search query! Please try again with a valid anime!")
                        .build()
                ).queueAfter(5, TimeUnit.SECONDS);
                ArrayList<Anime> animes = api.searchForAnime(query);
                if(animes == null || animes.isEmpty()) throw new Exception();
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
            catch(Exception ignored) {}
        }
        else if(args.get(0).equalsIgnoreCase("m") || args.get(0).equalsIgnoreCase("manga")) {
            String query = String.join(" ", args.subList(1, args.size()));
            InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
            api.searchMangas(query, msg.retrieveOriginal().complete());
        }
        else Tools.wrongUsage(event.getTextChannel(), this);
    }
}
