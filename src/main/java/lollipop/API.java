package lollipop;

import awatch.controller.AClient;
import awatch.controller.AListener;
import awatch.model.*;
import awatch.model.Character;
import lollipop.commands.Latest;
import lollipop.commands.RandomAnime;
import lollipop.commands.Top;
import lollipop.commands.search.Search;
import lollipop.commands.search.infos.Episodes;
import lollipop.commands.search.infos.News;
import lollipop.commands.trivia.Trivia;
import awatch.model.Question;
import lollipop.pages.AnimePage;
import lollipop.pages.EpisodeList;
import lollipop.pages.Newspaper;
import mread.controller.RClient;
import mread.controller.RListener;
import mread.model.Chapter;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
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
    final ArrayDeque<ButtonInteractionEvent> eventToReply = new ArrayDeque<>();
    final HashMap<ButtonInteractionEvent, AnimePage> eventToPage = new HashMap<>();

    /**
     * Searches for Manga given query
     * @param query manga name
     * @param message message
     */
    public void searchMangas(String query, InteractionHook message) {
        mangaClient.search(query);
        messageToEdit.push(message);
    }

    /**
     * Send Manga that was searched
     * @param mangas manga list
     */
    @Override
    public void sendMangas(List<Manga> mangas) {
        if(mangas.isEmpty()) {
            messageToEdit.removeFirst().editOriginalEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any results with that search query! Please try again with a valid manga!")
                            .build()).queue();
            return;
        }
        messageToEdit.removeFirst().editOriginalEmbeds(mangas.get(0).toEmbed().build()).queue();
    }

    /**
     * Send chapters from searched manga
     * @param manga manga
     */
    @Override
    public void sendChapters(Manga manga) {
        for (Chapter c : manga.chapters) System.out.println(c);
        mangaClient.pages(manga.chapters.get(0));
    }

    /**
     * Sent pages from a manga chapter
     * @param chapter chapter
     */
    @Override
    public void sendPages(Chapter chapter) {
        for (String s : chapter.pages) System.out.println(s);
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
     * @param message message
     * @param page anime page
     */
    public void getEpisodes(ButtonInteractionEvent event, InteractionHook message, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getEpisodes(id);
        eventToReply.push(event);
        messageToEdit.push(message);
        eventToPage.put(event, page);
    }

    /**
     * Retrieves recent news from the given anime
     * @param event button interaction event
     * @param message message
     * @param page anime page
     */
    public void getNews(ButtonInteractionEvent event, InteractionHook message, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getNews(id);
        eventToReply.push(event);
        messageToEdit.push(message);
        eventToPage.put(event, page);
    }

    /**
     * Retrieves statistics from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getStatistics(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getStatistics(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
    }

    /**
     * Retrieves themes from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getThemes(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getThemes(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
    }

    /**
     * Retrieves reccomendations from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getRecommendation(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getRecommendation(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
    }

    /**
     * Retrieves top review from the given anime
     * @param event button interaction event
     * @param page anime page
     */
    public void getReview(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getReview(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
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
        AnimePage page = Search.messageToPage.get(msg.getIdLong());
        page.timeout.cancel(false);
        page.animes = animes;
        animes.sort(Comparator.comparingInt(a -> {
            if(a.popularity < 1) return Integer.MAX_VALUE;
            return a.popularity;
        }));
        //this filters out 0 popularity search results rather than sending them to the back of the queue
        //animes = (ArrayList<Anime>) animes.stream().filter(a -> a.popularity > 0).collect(Collectors.toList());
        message.editOriginalEmbeds(animes.get(0).toEmbed().setFooter("Page 1/" + animes.size()).build()).setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.primary("episodes", Emoji.fromUnicode("⏯")).withLabel("Episodes"),
                Button.primary("more", Emoji.fromUnicode("\uD83D\uDD0E")).withLabel("More Info"),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
        message.editOriginalComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.primary("episodes", Emoji.fromUnicode("⏯")).withLabel("Episodes").asDisabled(),
                        Button.primary("more", Emoji.fromUnicode("\uD83D\uDD0E")).withLabel("More Info").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, i -> Search.messageToPage.remove(msg.getIdLong()));
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
        ButtonInteractionEvent event = eventToReply.removeFirst();
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
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
            String moreUrl = "";
            if(!episodes.get(0).url.equals("")) moreUrl = episodes.get(0).url.substring(0, episodes.get(0).url.length()-2);
            if(!sb.toString().equals("")) {
                sb.append("\n> [Click for all episodes!](").append(moreUrl).append(")");
                pages.add(sb);
            }
            else
                pages.get(pages.size()-1).append("\n> [Click for all episodes!](").append(moreUrl).append(")");

            message.editOriginalEmbeds(
                    new EmbedBuilder()
                            .setTitle("Episode List")
                            .setDescription(pages.get(0))
                            .setFooter("Page 1/" + pages.size())
                            .build()
            ).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).queue();
            AnimePage page = eventToPage.remove(event);
            page.episodes.put(page.pageNumber, new EpisodeList(pages,1, msg, event.getUser()));
            Episodes.messageToPage.put(msg.getIdLong(), page.episodes.get(page.pageNumber));
            message.editOriginalComponents()
                    .queueAfter(3, TimeUnit.MINUTES, i -> Episodes.messageToPage.remove(msg.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            message.editOriginalEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any episodes from that anime! Please try again later!")
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
        ButtonInteractionEvent event = eventToReply.removeFirst();
        InteractionHook message = messageToEdit.removeFirst();
        Message msg = message.retrieveOriginal().complete();
        try {
            if(articles == null || articles.isEmpty()) throw new Exception();
            Collections.reverse(articles);
            Message m = message.editOriginalEmbeds(articles.get(0).toEmbed().setFooter("Page 1/" + articles.size()).build()).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            AnimePage page = eventToPage.remove(event);
            page.news.put(page.pageNumber, new Newspaper(articles, 1, msg, event.getUser()));
            News.messageToPage.put(m.getIdLong(), page.news.get(page.pageNumber));
            m.editMessageComponents()
                    .queueAfter(3, TimeUnit.MINUTES, i -> News.messageToPage.remove(m.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            message.editOriginalEmbeds(
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
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(statistics.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.stats.put(page.pageNumber, statistics.toEmbed().build());
    }

    /**
     * Sends the requested themes
     * @param themes theme songs
     */
    @Override
    public void sendThemes(Themes themes) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(themes.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.themes.put(page.pageNumber, themes.toEmbed().build());
    }

    /**
     * Sends the requested recommendation list
     * @param recommendation recommendation list
     */
    @Override
    public void sendRecommendation(Recommendation recommendation) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(recommendation.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.recommendations.put(page.pageNumber, recommendation.toEmbed().build());
    }

    /**
     * Sends the requested top review
     * @param review top review
     */
    @Override
    public void sendReview(Review review) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(review.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.review.put(page.pageNumber, review.toEmbed().build());
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
        Top.messageToPage.get(msg.getIdLong()).animes = top;
        message.editOriginalComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, i -> Top.messageToPage.remove(msg.getIdLong()));
    }

    /**
     * Sends the latest requested animes
     * @param latest latest anime list
     */
    @Override
    public void sendLatest(ArrayList<Anime> latest) {
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
        catch(IOException e) {}
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
        Trivia.openGames.get(msg.getIdLong()).question = question;
        message.editOriginalEmbeds(question.toEmbed().build()).setActionRow(
                Button.secondary(question.correct.title.equals(question.options.get(0)) ? "right" : "wrong1", question.options.get(0)),
                Button.secondary(question.correct.title.equals(question.options.get(1)) ? "right" : "wrong2", question.options.get(1)),
                Button.secondary(question.correct.title.equals(question.options.get(2)) ? "right" : "wrong3", question.options.get(2)),
                Button.secondary(question.correct.title.equals(question.options.get(3)) ? "right" : "wrong4", question.options.get(3))
        ).queue();
        Trivia.openGames.get(msg.getIdLong()).gameTimeout = message.editOriginalEmbeds(new EmbedBuilder()
                .setColor(Color.red)
                .setDescription("Too late! You have to answer the trivia questions in under 15 seconds!")
                .setThumbnail("https://cdn.discordapp.com/emojis/738539027401146528.webp?size=80&quality=lossless")
                .build()
        ).setActionRows(Collections.emptyList()).queueAfter(15, TimeUnit.SECONDS, i -> Trivia.openGames.remove(msg.getIdLong()));
        Trivia.openGames.get(msg.getIdLong()).startTimeout.cancel(false);
        try {
            Cache.addTitleToCache(question.correct.title);
        }
        catch(IOException e) {}
    }

}
