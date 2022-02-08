package lollipop.commands;

import awatch.models.Anime;
import lollipop.*;
import lollipop.models.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                .addOptions(new OptionData(OptionType.STRING, "type", "anime / manga / character", true)
                        .addChoice("anime", "anime")
                        .addChoice("character", "character")
                        .addChoice("manga", "manga"))
                .addOption(OptionType.STRING, "query", "search query", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        if(args.size()<2) { Tools.wrongUsage(event, this); return; }
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
                ArrayList<Anime> animes = api.searchForAnime(query, event.getTextChannel().isNSFW());
                if(animes == null || animes.isEmpty()) throw new Exception();
                animes.sort(Comparator.comparingInt(a -> {
                    if(a.popularity == 0) return Integer.MAX_VALUE;
                    return a.popularity;
                }));
                //this filters out 0 popularity search results rather than sending them to the back of the queue
                //animes = (ArrayList<Anime>) animes.stream().filter(a -> a.popularity > 0).collect(Collectors.toList());
                Message m = msg.editOriginalEmbeds(Tools.animeToEmbed(animes.get(0)).setFooter("Page 1/" + animes.size()).build()).setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡")),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer")
                ).complete();
                messageToPage.put(m.getIdLong(), new AnimePage(animes, m, 1, event.getUser()));
                timeout.cancel(true);
                msg.editOriginalComponents()
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
        else Tools.wrongUsage(event, this);
    }
}
