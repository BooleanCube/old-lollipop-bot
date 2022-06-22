package lollipop;

import awatch.controller.AClient;
import awatch.controller.AListener;
import awatch.model.*;
import awatch.model.Character;
import lollipop.commands.Latest;
import lollipop.commands.Popular;
import lollipop.commands.RandomAnime;
import lollipop.commands.Top;
import lollipop.commands.search.Search;
import lollipop.commands.search.animecomps.Characters;
import lollipop.commands.search.animecomps.Episodes;
import lollipop.commands.search.animecomps.News;
import lollipop.commands.search.mangacomps.Chapters;
import lollipop.commands.trivia.TGame;
import lollipop.commands.trivia.Trivia;
import awatch.model.Question;
import lollipop.pages.*;
import mread.controller.RClient;
import mread.controller.RListener;
import mread.model.Chapter;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Contacts awatch and readm libraries to retrieve data
 */
public class API implements RListener, AListener {

    //manga
    final RClient mangaClient = new RClient(this);

    //anime
    final AClient animeClient = new AClient(this);

    //sending
    final ArrayDeque<InteractionHook> messageToEdit = new ArrayDeque<>();
    final ArrayDeque<SelectMenuInteractionEvent> eventToReply = new ArrayDeque<>();
    final HashMap<SelectMenuInteractionEvent, AnimePage> eventToAnimePage = new HashMap<>();
    final HashMap<SelectMenuInteractionEvent, MangaPage> eventToMangaPage = new HashMap<>();
    final HashMap<SelectMenuInteractionEvent, Chapter> eventToChapter = new HashMap<>();

    /**
     * Searches for Manga given query
     * @param query manga name
     * @param message message
     */
    public void searchMangas(String query, InteractionHook message) {
        mangaClient.search(query);
        messageToEdit.push(message);
    }

    public void getChapters(SelectMenuInteractionEvent event, MangaPage page) {
        Manga manga = page.mangas.get(page.pageNumber-1);
        mangaClient.chapters(manga);
        eventToReply.push(event);
        eventToMangaPage.put(event, page);
    }

    public void getPages(SelectMenuInteractionEvent event, Chapter chapter) {
        mangaClient.pages(chapter);
        eventToReply.push(event);
        eventToChapter.put(event, chapter);
    }

    /**
     * Send Manga that was searched
     * @param mangas manga list
     */
    @Override
    public void sendMangas(ArrayList<Manga> mangas) {
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        // this takes care of the none found possibility
        if(mangas.isEmpty()) return;
        MangaPage page = Search.messageToMangaPage.get(msg.getIdLong());
        page.timeout.cancel(false);
        page.mangas = mangas;
        page.mangas.sort(Comparator.comparingInt(m -> -m.views-(m.subscibers*10)));
        message.editOriginalEmbeds(
                mangas.get(0).toEmbed()
                        .setFooter("Page 1/"+mangas.size())
                        .build()
        ).setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder("Sort by popularity")
                                .addOption("Sort by popularity", "views")
                                .addOption("Sort by score", "score")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Chapters", "Chapters")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).complete();
        message.editOriginalComponents()
                .setActionRows(
                        ActionRow.of(
                                SelectMenu.create("order")
                                        .setPlaceholder("Disabled")
                                        .addOption("bruh", "really")
                                        .setDisabled(true)
                                        .build()
                        ),
                        ActionRow.of(
                                SelectMenu.create("components")
                                        .setPlaceholder("Disabled")
                                        .addOption("bruh", "really")
                                        .setDisabled(true)
                                        .build()
                        ),
                        ActionRow.of(
                                Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                                Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                        )
                ).queueAfter(3, TimeUnit.MINUTES, i -> {
                    Search.messageToMangaPage.remove(msg.getIdLong());
                    for(Manga manga : mangas) {
                        manga.chapters = null;
                    }
                });
    }

    /**
     * Send chapters from searched manga
     * @param chapters {@link ArrayList<Chapter>} chapter list
     */
    @Override
    public void sendChapters(ArrayList<Chapter> chapters) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        try {
            ArrayList<SelectMenu> menus = new ArrayList<>();
            SelectMenu.Builder currentMenu = null;
            int last = 0;
            for (int i = 0; i < chapters.size(); i++) {
                if (i % 25 == 0) {
                    if (i != 0) {
                        currentMenu.setPlaceholder(chapters.get(i - 25).title + " - " + chapters.get(i - 1).title);
                        last = i;
                        menus.add(currentMenu.build());
                    }
                    currentMenu = SelectMenu.create("menu" + i / 25);
                }
                currentMenu.addOption("Read " + chapters.get(i).title, Integer.toString(i));
            }
            if(currentMenu != null && !currentMenu.getOptions().isEmpty()) {
                currentMenu.setPlaceholder(chapters.get(last).title + " - " + chapters.get(chapters.size()-1).title);
                menus.add(currentMenu.build());
            }
            if (chapters.isEmpty()) throw new Exception();
            if(menus.isEmpty()) throw new Exception();

            MangaPage page = eventToMangaPage.remove(event);
            int pages = (int)Math.ceil(menus.size()/4.0);
            Manga manga = page.mangas.get(page.pageNumber - 1);
            InteractionHook msg = event.replyEmbeds(
                    new EmbedBuilder()
                            .setTitle(manga.title + " Chapter List")
                            .setDescription("If the pages are unreadable, you can click on the title of the embed for that chapter and read it in your browser of choice!")
                            .setFooter("Page 1/" + pages)
                            .build()
            ).setEphemeral(true).addActionRows(
                    ActionRow.of(menus.get(0)),
                    ActionRow.of(
                            1 >= menus.size() ?
                                    SelectMenu.create("disabled1")
                                            .setPlaceholder("Disabled")
                                            .addOption("bruh","really")
                                            .setDisabled(true)
                                            .build() :
                                    menus.get(1)
                    ),
                    ActionRow.of(
                            2 >= menus.size() ?
                                    SelectMenu.create("disabled2")
                                            .setPlaceholder("Disabled")
                                            .addOption("bruh","really")
                                            .setDisabled(true)
                                            .build() :
                                    menus.get(2)
                    ),
                    ActionRow.of(
                            3 >= menus.size() ?
                                    SelectMenu.create("disabled3")
                                            .setPlaceholder("Disabled")
                                            .addOption("bruh","really")
                                            .setDisabled(true)
                                            .build() :
                                    menus.get(3)
                    ),
                    ActionRow.of(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    )
            ).complete();

            Message message = msg.retrieveOriginal().complete();
            event.getMessage().editMessageComponents().setActionRows(
                    ActionRow.of(
                            SelectMenu.create("order")
                                    .setPlaceholder(page.currentPlaceholder)
                                    .addOption("Sort by popularity", "views")
                                    .addOption("Sort by score", "score")
                                    .build()
                    ),
                    ActionRow.of(
                            SelectMenu.create("components")
                                    .setPlaceholder("Show component")
                                    .addOption("Chapters", "Chapters")
                                    .build()
                    ),
                    ActionRow.of(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    )
            ).queue();

            manga.chapters = new ChapterList(chapters, menus, 1, pages, message, event.getUser());
            Chapters.messageToPage.put(message.getIdLong(), manga.chapters);
            msg.editOriginalComponents()
                    .setActionRows(
                            ActionRow.of(
                                    SelectMenu.create("disabled1")
                                            .setPlaceholder("Disabled")
                                            .addOption("bruh", "really")
                                            .setDisabled(true)
                                            .build()
                            ),
                            ActionRow.of(
                                    SelectMenu.create("disabled2")
                                            .setPlaceholder("Disabled")
                                            .addOption("bruh", "really")
                                            .setDisabled(true)
                                            .build()
                            ),
                            ActionRow.of(
                                    SelectMenu.create("disabled3")
                                            .setPlaceholder("Disabled")
                                            .addOption("bruh", "really")
                                            .setDisabled(true)
                                            .build()
                            ),
                            ActionRow.of(
                                    SelectMenu.create("disabled4")
                                            .setPlaceholder("Disabled")
                                            .addOption("bruh", "really")
                                            .setDisabled(true)
                                            .build()
                            ),
                            ActionRow.of(
                                    Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                                    Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                            )
                    ).queueAfter(25, TimeUnit.MINUTES, i -> Chapters.messageToPage.remove(message.getIdLong()));
        } catch (Exception e) {
            e.printStackTrace();
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setDescription("Could not find any chapters for this manga!")
                            .setColor(Color.red)
                            .build()
            ).setEphemeral(true).queue();
        }
    }

    /**
     * Sent pages from a manga chapter
     * @param pages {@link ArrayList<String>} list of urls to pages
     */
    @Override
    public void sendPages(ArrayList<String> pages) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        Chapter chapter = eventToChapter.remove(event);
        try {
            if(pages == null || pages.isEmpty()) throw new Exception();
            chapter.pages = pages;

            InteractionHook message = event.replyEmbeds(
                    chapter.toEmbed()
                            .build()
            ).setEphemeral(true).addActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            SelectMenu menu = event.getSelectMenu();
            event.editSelectMenu(
                    menu.createCopy()
                            .build()
            ).queue();

            Message msg = message.retrieveOriginal().complete();
            chapter.user = event.getUser();
            ChapterList.messageToChapter.put(msg.getIdLong(), chapter);
            message.editOriginalComponents()
                    .queueAfter(5, TimeUnit.MINUTES, i -> ChapterList.messageToChapter.remove(msg.getIdLong()));
        } catch(Exception e) {
            e.printStackTrace();
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setDescription("Could not find any pages for this chapter!\n> Try reading [" + chapter.title + "](" + chapter.url + ") from the website.")
                            .setColor(Color.red)
                            .build()
            ).setEphemeral(true).queue();
        }
    }

    /**
     * Searches for an anime given a query and more information
     * @param message message
     * @param query anime name
     * @param nsfw nsfw allowed
     */
    public void searchAnime(InteractionHook message, String query, boolean nsfw) {
        animeClient.searchAnime(query, nsfw);
        messageToEdit.push(message);
    }

    /**
     * Searches for a character given a query
     * @param message message
     * @param query character name
     */
    public void searchCharacter(InteractionHook message, String query) {
        animeClient.searchCharacter(query);
        messageToEdit.push(message);
    }

    /**
     * Generates a randomly chosen anime related quote
     * @param message message
     */
    public void randomQuote(InteractionHook message) {
        animeClient.randomQuote();
        messageToEdit.push(message);
    }

    /**
     * Retrieves the episodes from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getEpisodes(SelectMenuInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getEpisodes(id);
        eventToReply.push(event);
        eventToAnimePage.put(event, page);
    }

    /**
     * Retrieves the characters from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getCharacters(SelectMenuInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getCharacters(id);
        eventToReply.push(event);
        eventToAnimePage.put(event, page);
    }

    /**
     * Retrieves recent news from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getNews(SelectMenuInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getNews(id);
        eventToReply.push(event);
        eventToAnimePage.put(event, page);
    }

    /**
     * Retrieves statistics from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getStatistics(SelectMenuInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getStatistics(id);
        eventToReply.push(event);
        eventToAnimePage.put(event, page);
    }

    /**
     * Retrieves themes from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getThemes(SelectMenuInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getThemes(id);
        eventToReply.push(event);
        eventToAnimePage.put(event, page);
    }

    /**
     * Retrieves reccomendations from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getRecommendation(SelectMenuInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getRecommendation(id);
        eventToReply.push(event);
        eventToAnimePage.put(event, page);
    }

    /**
     * Retrieves top review from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getReview(SelectMenuInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getReview(id);
        eventToReply.push(event);
        eventToAnimePage.put(event, page);
    }

    /**
     * Retrieves the top 25 ranked animes in terms of score
     * @param message message
     */
    public void getTop(InteractionHook message) {
        animeClient.getTop();
        messageToEdit.push(message);
    }

    /**
     * Retrieves the top 25 ranked animes in terms of popularity
     * @param message message
     */
    public void getPopularAnime(InteractionHook message) {
        animeClient.getPopular();
        messageToEdit.push(message);
    }

    /**
     * Retrieves the latest anime of the current season
     * @param message message
     */
    public void getLatest(InteractionHook message) {
        animeClient.getLatest();
        messageToEdit.push(message);
    }

    /**
     * Generates a randomly chosen anime from MALs database
     * @param message message
     * @param nsfw nsfw allowed
     */
    public void randomAnime(InteractionHook message, boolean nsfw) {
        animeClient.randomAnime(nsfw);
        messageToEdit.push(message);
    }

    /**
     * Generates a randomly chosen anime related GIF
     * @param message message
     */
    public void randomGIF(InteractionHook message) {
        animeClient.randomGIF();
        messageToEdit.push(message);
    }

    /**
     * Gets a random trivia question for the trivia game
     * @param message message
     */
    public void randomTrivia(InteractionHook message) {
        HashSet<String> available = new HashSet<>(Cache.titles) {

            /** Every call to iterator() will give a possibly unique iteration order, or not */
            @Nonnull
            @Override
            public Iterator<String> iterator() {
                return new RandomizingIterator<>(super.iterator());
            }
            class RandomizingIterator<T> implements Iterator<T> {
                final Iterator<T> iterator;
                private RandomizingIterator(@Nonnull final Iterator<T> iterator) {
                    List<T> list = new ArrayList<>();
                    while(iterator.hasNext()) list.add(iterator.next());
                    Collections.shuffle(list);
                    this.iterator = list.iterator();
                }
                @Override
                public boolean hasNext() {
                    return this.iterator.hasNext();
                }
                @Override
                public T next() {
                    return this.iterator.next();
                }
            }

        };
        animeClient.randomTrivia(available);
        messageToEdit.push(message);
    }

    /**
     * Sends the requested anime list
     * @param animes anime list
     */
    @Override
    public void sendSearchAnime(ArrayList<Anime> animes) {
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        if(animes == null || animes.isEmpty()) return;
        AnimePage page = Search.messageToAnimePage.get(msg.getIdLong());
        page.timeout.cancel(false);
        page.animes = animes;
        page.animes.sort(Comparator.comparingInt(a -> {
            if(a.popularity < 1) return Integer.MAX_VALUE;
            return a.popularity;
        }));
        message.editOriginalEmbeds(animes.get(0).toEmbed().setFooter("Page 1/" + animes.size()).build()).setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder("Sort by popularity")
                                .addOption("Sort by popularity", "popularity")
                                .addOption("Sort by ranks", "ranks")
                                .addOption("Sort by latest", "time")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Trailer", "Trailer")
                                .addOption("Episodes", "Episodes")
                                .addOption("Characters", "Characters")
                                .addOption("Themes", "Themes")
                                .addOption("Recommendations", "Recommendations")
                                .addOption("News", "News")
                                .addOption("Review", "Review")
                                .addOption("Statistics", "Statistics")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).complete();
        message.editOriginalComponents()
                .setActionRows(
                        ActionRow.of(
                                SelectMenu.create("order")
                                        .setPlaceholder("Disabled")
                                        .addOption("bruh", "really")
                                        .setDisabled(true)
                                        .build()
                        ),
                        ActionRow.of(
                                SelectMenu.create("components")
                                        .setPlaceholder("Disabled")
                                        .addOption("bruh", "really")
                                        .setDisabled(true)
                                        .build()
                        ),
                        ActionRow.of(
                                Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                                Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                        )
                ).queueAfter(3, TimeUnit.MINUTES, i -> {
                    Search.messageToAnimePage.remove(msg.getIdLong());
                    for(Anime anime : animes) {
                        anime.news = null;
                        anime.stats = null;
                        anime.episodeList = null;
                        anime.themes = null;
                        anime.review = null;
                        anime.recommendations = null;
                    }
                });
    }

    /**
     * Sends the requested character
     * @param character character
     */
    @Override
    public void sendSearchCharacter(Character character) {
        InteractionHook message = messageToEdit.removeFirst();
        message.editOriginalEmbeds(character.toEmbed().build()).queue();
    }

    /**
     * Sends the requested quote
     * @param quote random quote
     */
    @Override
    public void sendRandomQuote(Quote quote) {
        InteractionHook message = messageToEdit.removeFirst();
        message.editOriginalEmbeds(quote.toEmbed().build()).queue();
    }

    /**
     * Sends the requested episodes
     * @param episodes episodes
     */
    @Override
    public void sendEpisodes(ArrayList<Episode> episodes) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        try {
            ArrayList<StringBuilder> pages = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<episodes.size(); i++) {
                if(i%10 == 0 && i != 0) {
                    pages.add(sb);
                    sb = new StringBuilder();
                }
                sb.append("Episode #").append(i+1).append(" - [").append(episodes.get(i).title)
                        .append("](").append(episodes.get(i).url).append(")\n");
            }
            if(episodes.size() == 0) sb.append("Could not find any episodes on MAL!");
            String moreUrl = "";
            if(episodes.size() > 0 && !episodes.get(0).url.equals("")) moreUrl = episodes.get(0).url.substring(0, episodes.get(0).url.length()-2);
            if(!sb.toString().equals("") && !moreUrl.equals("")) {
                sb.append("\n> [Click for all episodes!](").append(moreUrl).append(")");
                pages.add(sb);
            }
            else if(pages.size()>0 && !moreUrl.equals(""))
                pages.get(pages.size()-1).append("\n> [Click for all episodes!](").append(moreUrl).append(")");

            InteractionHook msg = event.replyEmbeds(
                    new EmbedBuilder()
                            .setTitle("Episode List")
                            .setDescription(pages.get(0))
                            .setFooter("Page 1/" + pages.size())
                            .build()
            ).setEphemeral(true).addActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();

            Message message = msg.retrieveOriginal().complete();
            AnimePage page = eventToAnimePage.remove(event);
            event.getMessage().editMessageComponents().setActionRows(
                    ActionRow.of(
                            SelectMenu.create("order")
                                    .setPlaceholder(page.currentPlaceholder)
                                    .addOption("Sort by popularity", "popularity")
                                    .addOption("Sort by ranks", "ranks")
                                    .addOption("Sort by latest", "time")
                                    .build()
                    ),
                    ActionRow.of(
                            SelectMenu.create("components")
                                    .setPlaceholder("Show component")
                                    .addOption("Trailer", "Trailer")
                                    .addOption("Episodes", "Episodes")
                                    .addOption("Characters", "Characters")
                                    .addOption("Themes", "Themes")
                                    .addOption("Recommendations", "Recommendations")
                                    .addOption("News", "News")
                                    .addOption("Review", "Review")
                                    .addOption("Statistics", "Statistics")
                                    .build()
                    ),
                    ActionRow.of(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    )
            ).queue();
            page.animes.get(page.pageNumber-1).episodeList = new EpisodeList(pages,1, message, event.getUser());
            Episodes.messageToPage.put(message.getIdLong(), page.animes.get(page.pageNumber-1).episodeList);
            msg.editOriginalComponents()
                    .queueAfter(3, TimeUnit.MINUTES, i -> Episodes.messageToPage.remove(message.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            if(event.isAcknowledged()) return;
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any episodes from that anime! Please try again later!")
                            .build()
            ).queue();
        }
    }

    /**
     * Sends the requested anime character list
     * @param characters episodes
     */
    @Override
    public void sendCharacterList(ArrayList<Character> characters) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        try {
            ArrayList<StringBuilder> pages = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<characters.size(); i++) {
                if(i%10 == 0 && i != 0) {
                    pages.add(sb);
                    sb = new StringBuilder();
                }
                sb.append("[").append(characters.get(i).name)
                        .append("](").append(characters.get(i).url).append(") (").append(characters.get(i).role).append(" character)\n");
            }
            if(characters.size() == 0) sb.append("Could not find any characters for this anime on MAL!");

            InteractionHook msg = event.replyEmbeds(
                    new EmbedBuilder()
                            .setTitle("Character List")
                            .setDescription(pages.get(0))
                            .setFooter("Page 1/" + pages.size())
                            .build()
            ).setEphemeral(true).addActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();

            Message message = msg.retrieveOriginal().complete();
            AnimePage page = eventToAnimePage.remove(event);
            event.getMessage().editMessageComponents().setActionRows(
                    ActionRow.of(
                            SelectMenu.create("order")
                                    .setPlaceholder(page.currentPlaceholder)
                                    .addOption("Sort by popularity", "popularity")
                                    .addOption("Sort by ranks", "ranks")
                                    .addOption("Sort by latest", "time")
                                    .build()
                    ),
                    ActionRow.of(
                            SelectMenu.create("components")
                                    .setPlaceholder("Show component")
                                    .addOption("Trailer", "Trailer")
                                    .addOption("Episodes", "Episodes")
                                    .addOption("Characters", "Characters")
                                    .addOption("Themes", "Themes")
                                    .addOption("Recommendations", "Recommendations")
                                    .addOption("News", "News")
                                    .addOption("Review", "Review")
                                    .addOption("Statistics", "Statistics")
                                    .build()
                    ),
                    ActionRow.of(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    )
            ).queue();
            page.animes.get(page.pageNumber-1).characterList = new CharacterList(pages,1, message, event.getUser());
            Characters.messageToPage.put(message.getIdLong(), page.animes.get(page.pageNumber-1).characterList);
            msg.editOriginalComponents()
                    .queueAfter(3, TimeUnit.MINUTES, i -> Characters.messageToPage.remove(message.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            if(event.isAcknowledged()) return;
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any characters from that anime! Please try again later!")
                            .build()
            ).queue();
        }
    }

    /**
     * Sends the requested news articles
     * @param articles article list
     */
    @Override
    public void sendNews(ArrayList<Article> articles) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        try {
            if(articles == null || articles.isEmpty()) throw new Exception();
            Collections.reverse(articles);
            InteractionHook m = event.replyEmbeds(
                    articles.get(0).toEmbed()
                            .setFooter("Page 1/" + articles.size())
                            .build()
            ).setEphemeral(true).addActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            Message message = m.retrieveOriginal().complete();
            AnimePage page = eventToAnimePage.remove(event);
            event.getMessage().editMessageComponents().setActionRows(
                    ActionRow.of(
                            SelectMenu.create("order")
                                    .setPlaceholder(page.currentPlaceholder)
                                    .addOption("Sort by popularity", "popularity")
                                    .addOption("Sort by ranks", "ranks")
                                    .addOption("Sort by latest", "time")
                                    .build()
                    ),
                    ActionRow.of(
                            SelectMenu.create("components")
                                    .setPlaceholder("Show component")
                                    .addOption("Trailer", "Trailer")
                                    .addOption("Episodes", "Episodes")
                                    .addOption("Characters", "Characters")
                                    .addOption("Themes", "Themes")
                                    .addOption("Recommendations", "Recommendations")
                                    .addOption("News", "News")
                                    .addOption("Review", "Review")
                                    .addOption("Statistics", "Statistics")
                                    .build()
                    ),
                    ActionRow.of(
                            Button.secondary("left", Emoji.fromUnicode("⬅")),
                            Button.secondary("right", Emoji.fromUnicode("➡"))
                    )
            ).queue();
            page.animes.get(page.pageNumber-1).news = new Newspaper(articles, 1, message, event.getUser());
            News.messageToPage.put(message.getIdLong(), page.animes.get(page.pageNumber-1).news);
            m.editOriginalComponents()
                    .queueAfter(3, TimeUnit.MINUTES, i -> News.messageToPage.remove(message.getIdLong()));
        }
        catch (Exception e) {
            if(event.isAcknowledged()) return;
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any articles from that anime! Please try again later!")
                            .build()
            ).queue();
        }
    }

    /**
     * Sends the requested statistics
     * @param statistics statistics
     */
    @Override
    public void sendStatistics(Statistic statistics) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(statistics.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToAnimePage.remove(event);
        event.getMessage().editMessageComponents().setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder(page.currentPlaceholder)
                                .addOption("Sort by popularity", "popularity")
                                .addOption("Sort by ranks", "ranks")
                                .addOption("Sort by latest", "time")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Trailer", "Trailer")
                                .addOption("Episodes", "Episodes")
                                .addOption("Characters", "Characters")
                                .addOption("Themes", "Themes")
                                .addOption("Recommendations", "Recommendations")
                                .addOption("News", "News")
                                .addOption("Review", "Review")
                                .addOption("Statistics", "Statistics")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).queue();
        page.animes.get(page.pageNumber-1).stats = statistics.toEmbed().build();
    }

    /**
     * Sends the requested themes
     * @param themes theme songs
     */
    @Override
    public void sendThemes(Themes themes) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(themes.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToAnimePage.remove(event);
        event.getMessage().editMessageComponents().setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder(page.currentPlaceholder)
                                .addOption("Sort by popularity", "popularity")
                                .addOption("Sort by ranks", "ranks")
                                .addOption("Sort by latest", "time")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Trailer", "Trailer")
                                .addOption("Episodes", "Episodes")
                                .addOption("Characters", "Characters")
                                .addOption("Themes", "Themes")
                                .addOption("Recommendations", "Recommendations")
                                .addOption("News", "News")
                                .addOption("Review", "Review")
                                .addOption("Statistics", "Statistics")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).queue();
        page.animes.get(page.pageNumber-1).themes = themes.toEmbed().build();
    }

    /**
     * Sends the requested recommendation list
     * @param recommendation recommendation list
     */
    @Override
    public void sendRecommendation(Recommendation recommendation) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(recommendation.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToAnimePage.remove(event);
        event.getMessage().editMessageComponents().setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder(page.currentPlaceholder)
                                .addOption("Sort by popularity", "popularity")
                                .addOption("Sort by ranks", "ranks")
                                .addOption("Sort by latest", "time")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Trailer", "Trailer")
                                .addOption("Episodes", "Episodes")
                                .addOption("Characters", "Characters")
                                .addOption("Themes", "Themes")
                                .addOption("Recommendations", "Recommendations")
                                .addOption("News", "News")
                                .addOption("Review", "Review")
                                .addOption("Statistics", "Statistics")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).queue();
        page.animes.get(page.pageNumber-1).recommendations = recommendation.toEmbed().build();
    }

    /**
     * Sends the requested top review
     * @param review top review
     */
    @Override
    public void sendReview(Review review) {
        SelectMenuInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(review.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToAnimePage.remove(event);
        event.getMessage().editMessageComponents().setActionRows(
                ActionRow.of(
                        SelectMenu.create("order")
                                .setPlaceholder(page.currentPlaceholder)
                                .addOption("Sort by popularity", "popularity")
                                .addOption("Sort by ranks", "ranks")
                                .addOption("Sort by latest", "time")
                                .build()
                ),
                ActionRow.of(
                        SelectMenu.create("components")
                                .setPlaceholder("Show component")
                                .addOption("Trailer", "Trailer")
                                .addOption("Episodes", "Episodes")
                                .addOption("Characters", "Characters")
                                .addOption("Themes", "Themes")
                                .addOption("Recommendations", "Recommendations")
                                .addOption("News", "News")
                                .addOption("Review", "Review")
                                .addOption("Statistics", "Statistics")
                                .build()
                ),
                ActionRow.of(
                        Button.secondary("left", Emoji.fromUnicode("⬅")),
                        Button.secondary("right", Emoji.fromUnicode("➡"))
                )
        ).queue();
        page.animes.get(page.pageNumber-1).review = review.toEmbed().build();
    }

    /**
     * Sends the top 25 requested animes
     * @param top top anime list
     */
    @Override
    public void sendTop(ArrayList<Anime> top) {
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        if(top == null) return;
        AnimePage page = Top.messageToPage.get(msg.getIdLong());
        page.timeout.cancel(false);
        message.editOriginalEmbeds(top.get(0).toEmbed().setFooter("Page 1/" + top.size()).build()).setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
        page.animes = top;
        message.editOriginalComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, i -> Top.messageToPage.remove(msg.getIdLong()));
    }

    /**
     * Sends the 25 most popular requested animes
     * @param popular popular anime list
     */
    @Override
    public void sendPopularAnime(ArrayList<Anime> popular) {
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        if(popular == null) return;
        AnimePage page = Popular.messageToPage.get(msg.getIdLong());
        page.timeout.cancel(false);
        message.editOriginalEmbeds(popular.get(0).toEmbed().setFooter("Page 1/" + popular.size()).build()).setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
        page.animes = popular;
        message.editOriginalComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, i -> Popular.messageToPage.remove(msg.getIdLong()));
    }

    /**
     * Sends the latest requested animes
     * @param latest latest anime list
     */
    @Override
    public void sendLatestAnime(ArrayList<Anime> latest) {
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        if(latest == null) return;
        AnimePage page = Latest.messageToPage.get(msg.getIdLong());
        page.timeout.cancel(false);
        message.editOriginalEmbeds(latest.get(0).toEmbed().setFooter("Page 1/" + latest.size()).build()).setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
        Latest.messageToPage.get(msg.getIdLong()).animes = latest;
        message.editOriginalComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, i -> Latest.messageToPage.remove(msg.getIdLong()));
    }

    /**
     * Sends a random anime
     * @param random random anime
     */
    @Override
    public void sendRandomAnime(Anime random) {
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        if(random == null) return;
        AnimePage page = RandomAnime.messageToPage.get(msg.getIdLong());
        page.timeout.cancel(false);
        page.animes.add(random);
        message.editOriginalEmbeds(random.toEmbed().build()).setActionRow(
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer")
        ).queue();
        message.editOriginalComponents()
                .queueAfter(3, TimeUnit.MINUTES, i -> RandomAnime.messageToPage.remove(msg.getIdLong()));
        try {
            Cache.addTitleToCache(random.title);
        }
        catch(IOException ignored) {}
    }

    /**
     * Sends a random GIF
     * @param gif random gif
     */
    @Override
    public void sendRandomGIF(GIF gif) {
        InteractionHook message = messageToEdit.removeFirst();
        if(gif == null) return;
        message.editOriginalEmbeds(gif.toEmbed().build()).queue();
    }

    /**
     * Sends the found trivia question in the channel
     * @param question random trivia question
     */
    @Override
    public void sendTrivia(Question question) {
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        if (question.correct == null) return;
        TGame game = Trivia.openGames.get(msg.getIdLong());
        game.question = question;
        message.editOriginalEmbeds(question.toEmbed().build()).setActionRow(
                Button.secondary(question.correct.title.equals(question.options.get(0)) ? "right" : "wrong1", question.options.get(0)),
                Button.secondary(question.correct.title.equals(question.options.get(1)) ? "right" : "wrong2", question.options.get(1)),
                Button.secondary(question.correct.title.equals(question.options.get(2)) ? "right" : "wrong3", question.options.get(2)),
                Button.secondary(question.correct.title.equals(question.options.get(3)) ? "right" : "wrong4", question.options.get(3))
        ).queue();
        Runnable success = () -> {
            int xp = (int)((int)(Math.random()*11)-20/Constant.MULTIPLIER);
            game.gameTimeout = message.editOriginalEmbeds(new EmbedBuilder()
                    .setColor(Color.red)
                    .setTitle("Too late!")
                    .setDescription("You have to answer the trivia questions in under 15 seconds!")
                    .setThumbnail("https://cdn.discordapp.com/emojis/738539027401146528.webp?size=80&quality=lossless")
                    .setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                    .build()
            ).setActionRows(Collections.emptyList()).queueAfter(15, TimeUnit.SECONDS, i -> {
                Trivia.openGames.remove(msg.getIdLong());
                Database.addToUserBalance(game.user.getId(), xp);
            });
        };
        Runnable failure = () -> {
            int xp = (int)(Math.random()*11)-20;
            game.gameTimeout = message.editOriginalEmbeds(new EmbedBuilder()
                    .setColor(Color.red)
                    .setTitle("Too late!")
                    .setDescription("You have to answer the trivia questions in under 15 seconds!")
                    .setThumbnail("https://cdn.discordapp.com/emojis/738539027401146528.webp?size=80&quality=lossless")
                    .setFooter("You lost " + (-1*xp) + " lollipops!", "https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                    .build()
            ).setActionRows(Collections.emptyList()).queueAfter(15, TimeUnit.SECONDS, i -> {
                Trivia.openGames.remove(msg.getIdLong());
                Database.addToUserBalance(game.user.getId(), xp);
            });
        };
        BotStatistics.sendMultiplier(game.user.getId(), success, failure);
        Trivia.openGames.get(msg.getIdLong()).startTimeout.cancel(false);
        try {
            Cache.addTitleToCache(question.correct.title);
        } catch(IOException ignored) {}
    }

}
