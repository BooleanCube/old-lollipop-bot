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

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Contacts awatch and readm libraries to retrieve data
 */
public class API implements RListener, AListener {

    //manga
    public static HashMap<String, List<Manga>> mangaCache = new HashMap<>();
    final RClient mangaClient = new RClient(this);

    //anime
    final AClient animeClient = new AClient(this);

    //sending
    final ArrayDeque<InteractionHook> messageToEdit = new ArrayDeque<>();
    final ArrayDeque<ButtonInteractionEvent> eventToReply = new ArrayDeque<>();
    final HashMap<ButtonInteractionEvent, AnimePage> eventToPage = new HashMap<>();

    public void searchMangas(String query, InteractionHook message) {
        if(mangaCache.containsKey(query)) {
            message.editOriginalEmbeds(Tools.mangaToEmbed(mangaCache.get(query).get(0)).build()).queue();
            return;
        }
        mangaClient.search(query);
        messageToEdit.push(message);
    }

    @Override
    public void sendMangas(List<Manga> mangas) {
        if(mangas.isEmpty()) { messageToEdit.removeFirst().editOriginalEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Could not find any results with that search query! Please try again with a valid manga!").build()).queue(); return; }
        messageToEdit.removeFirst().editOriginalEmbeds(Tools.mangaToEmbed(mangas.get(0)).build()).queue();
    }

    @Override
    public void sendChapters(Manga manga) {
        for (Chapter c : manga.chapters) System.out.println(c);
        mangaClient.pages(manga.chapters.get(0));
    }

    @Override
    public void sendPages(Chapter chapter) {
        for (String s : chapter.pages) System.out.println(s);
    }

    public void searchAnime(InteractionHook message, String query, boolean nsfw) {
        animeClient.searchAnime(query, nsfw);
        messageToEdit.push(message);
    }

    public void searchCharacter(InteractionHook message, String query) {
        animeClient.searchCharacter(query);
        messageToEdit.push(message);
    }

    public void randomQuote(InteractionHook message) {
        animeClient.randomQuote();
        messageToEdit.push(message);
    }

    public void getEpisodes(ButtonInteractionEvent event, InteractionHook message, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getEpisodes(id);
        eventToReply.push(event);
        messageToEdit.push(message);
        eventToPage.put(event, page);
    }

    public void getNews(ButtonInteractionEvent event, InteractionHook message, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getNews(id);
        eventToReply.push(event);
        messageToEdit.push(message);
        eventToPage.put(event, page);
    }

    public void getStatistics(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getStatistics(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
    }

    public void getThemes(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getThemes(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
    }

    public void getRecommendation(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getRecommendation(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
    }

    public void getReview(ButtonInteractionEvent event, AnimePage page) {
        long id = page.animes.get(page.pageNumber-1).malID;
        animeClient.getReview(id);
        eventToReply.push(event);
        eventToPage.put(event, page);
    }

    public void getTop(InteractionHook message) {
        animeClient.getTop();
        messageToEdit.push(message);
    }

    public void getLatest(InteractionHook message) {
        animeClient.getLatest();
        messageToEdit.push(message);
    }

    public void randomAnime(InteractionHook message, boolean nsfw) {
        animeClient.randomAnime(nsfw);
        messageToEdit.push(message);
    }

    public void randomGIF(InteractionHook message) {
        animeClient.randomGIF();
        messageToEdit.push(message);
    }

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
                .queueAfter(3, TimeUnit.MINUTES, me -> Search.messageToPage.remove(msg.getIdLong()));
    }

    @Override
    public void sendSearchCharacter(Character character) {
        InteractionHook message = messageToEdit.removeFirst();
        message.editOriginalEmbeds(character.toEmbed().build()).queue();
    }

    @Override
    public void sendRandomQuote(Quote quote) {
        InteractionHook message = messageToEdit.removeFirst();
        message.editOriginalEmbeds(quote.toEmbed().build()).queue();
    }

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
                    .queueAfter(3, TimeUnit.MINUTES, me -> Episodes.messageToPage.remove(msg.getIdLong()));
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
                    .queueAfter(3, TimeUnit.MINUTES, me -> News.messageToPage.remove(m.getIdLong()));
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

    @Override
    public void sendStatistics(Statistic statistics) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(statistics.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.stats.put(page.pageNumber, statistics.toEmbed().build());
    }

    @Override
    public void sendThemes(Themes themes) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(themes.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.themes.put(page.pageNumber, themes.toEmbed().build());
    }

    @Override
    public void sendRecommendation(Recommendation recommendation) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(recommendation.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.stats.put(page.pageNumber, recommendation.toEmbed().build());
    }

    @Override
    public void sendReview(Review review) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(review.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = eventToPage.remove(event);
        page.review.put(page.pageNumber, review.toEmbed().build());
    }

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
                .queueAfter(3, TimeUnit.MINUTES, me -> Top.messageToPage.remove(msg.getIdLong()));
    }

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
                .queueAfter(3, TimeUnit.MINUTES, me -> Latest.messageToPage.remove(msg.getIdLong()));
    }

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
                .queueAfter(3, TimeUnit.MINUTES, me -> RandomAnime.messageToPage.remove(msg.getIdLong()));
    }

    @Override
    public void sendRandomGIF(GIF gif) {
        InteractionHook message = messageToEdit.removeFirst();
        if(gif == null) return;
        message.editOriginalEmbeds(gif.toEmbed().build()).queue();
    }

}
