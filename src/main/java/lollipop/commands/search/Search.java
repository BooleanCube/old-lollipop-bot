package lollipop.commands.search;

import lollipop.*;
import lollipop.pages.AnimePage;
import lollipop.pages.CharacterPage;
import lollipop.pages.MangaPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.Color;
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

    public static HashMap<Long, AnimePage> messageToAnimePage = new HashMap<>();
    public static HashMap<Long, MangaPage> messageToMangaPage = new HashMap<>();
    public static HashMap<Long, CharacterPage> messageToCharacterPage = new HashMap<>();
    public static HashMap<Long, ScheduledFuture<?>> messageToUserTimeout = new HashMap<>();

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this).addOptions(
                new OptionData(OptionType.STRING, "type", "anime / manga / character / user", true)
                        .addChoice("anime", "anime")
                        .addChoice("character", "character")
                        .addChoice("manga", "manga")
                        .addChoice("user", "user")
        ).addOption(OptionType.STRING, "query", "search query", true);
    }

    static API api = new API();

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if(!event.getInteraction().isFromGuild()) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("This command can only be used in guilds for safety reasons!")
                            .build()
            ).queue();
            return;
        }

        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        if(args.size()<2) { Tools.wrongUsage(event, this); return; }

        if(args.get(0).equalsIgnoreCase("character")) {
            try {
                String query = String.join(" ", args.subList(1, args.size()));
                InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
                ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("""
                                Could not find a character with that search query! Try:
                                > Search using japanese title (eg. Kuujou Joutarou)
                                > Search with a valid character name that exists in MyAnimeList's database
                                """)
                        .build()
                ).queueAfter(5, TimeUnit.SECONDS);
                Message m = msg.retrieveOriginal().complete();
                messageToCharacterPage.put(m.getIdLong(), new CharacterPage(null, m, 1, event.getUser(), timeout));
                api.searchCharacter(msg, query);
            } catch(Exception ignored) {}
        }

        else if(args.get(0).equalsIgnoreCase("anime")) {
            try {
                String query = String.join(" ", args.subList(1, args.size()));
                InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
                ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("""
                                Could not find an anime with that search query! Try:
                                > Search using japanese title (eg. Kimetsu no Yaiba)
                                > Search with a valid anime title that exists in MyAnimeList's database
                                > NSFW Animes only appear in NSFW channels""")
                        .build()
                ).queueAfter(5, TimeUnit.SECONDS);
                Message m = msg.retrieveOriginal().complete();
                messageToAnimePage.put(m.getIdLong(), new AnimePage(null, m, 1, event.getUser(), timeout));
                api.searchAnime(msg, query, event.getTextChannel().isNSFW());
            }
            catch(Exception ignored) {}
        }

        else if(args.get(0).equalsIgnoreCase("manga")) {
            String query = String.join(" ", args.subList(1, args.size()));
            InteractionHook msg = event.replyEmbeds(
                    new EmbedBuilder()
                            .setDescription("Searching for `" + query + "`...")
                            .build()
            ).complete();
            ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                    .setColor(Color.red)
                    .setDescription("""
                                Could not find a manga with that search query! Try:
                                > Search using japanese title (eg. Kimetsu no Yaiba)
                                > Search with a valid manga title that exists in Readm's database
                                > There was a small error, just try again.
                                """)
                    .build()
            ).queueAfter(5, TimeUnit.SECONDS);
            Message m = msg.retrieveOriginal().complete();
            messageToMangaPage.put(m.getIdLong(), new MangaPage(null, m, 1, event.getUser(), timeout));
            api.searchMangas(query, msg);
        }

        else if(args.get(0).equalsIgnoreCase("user")) {
            try {
                String query = String.join(" ", args.subList(1, args.size()));
                InteractionHook msg = event.replyEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
                ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("Could not find a user with that username on MAL!")
                        .build()
                ).queueAfter(5, TimeUnit.SECONDS);
                Message m = msg.retrieveOriginal().complete();
                messageToUserTimeout.put(m.getIdLong(), timeout);
                api.searchUser(msg, query);
            }
            catch(Exception ignored) {}
        }

        else Tools.wrongUsage(event, this);
    }

}
