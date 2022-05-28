package lollipop.commands.search;

import awatch.model.Anime;
import lollipop.*;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
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
        return "Searches for an anime/manga/charcater and NSFW content is locked to NSFW channels only!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [anime/manga/character] [query]`";
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
            api.searchCharacter(msg, query);
        }

        else if(args.get(0).equalsIgnoreCase("a") || args.get(0).equalsIgnoreCase("anime")) {
            try {
                String query = String.join(" ", args.subList(1, args.size()));
                InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
                ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("""
                                Could not find an anime with that search query!
                                > Search using japanese title (eg. Kimetsu no Yaiba)
                                > Try again with a valid anime title that exists in MyAnimeList's database""")
                        .build()
                ).queueAfter(5, TimeUnit.SECONDS);
                Message m = msg.retrieveOriginal().complete();
                Search.messageToPage.put(m.getIdLong(), new AnimePage(null, m, 1, event.getUser(), timeout));
                api.searchAnime(msg, query, event.getTextChannel().isNSFW());
            }
            catch(Exception ignored) {}
        }

        else if(args.get(0).equalsIgnoreCase("m") || args.get(0).equalsIgnoreCase("manga")) {
            String query = String.join(" ", args.subList(1, args.size()));
            InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
            api.searchMangas(query, msg);
        }

        else Tools.wrongUsage(event, this);
    }

}
