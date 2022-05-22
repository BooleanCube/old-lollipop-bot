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
import mread.controller.RClient;
import mread.controller.RListener;
import mread.model.Chapter;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
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
    final ArrayDeque<Message> messageToEdit = new ArrayDeque<>();
    final ArrayDeque<ButtonInteractionEvent> eventToReply = new ArrayDeque<>();

    public void searchMangas(String query, Message message) {
        if(mangaCache.containsKey(query)) {
            message.editMessageEmbeds(Tools.mangaToEmbed(mangaCache.get(query).get(0)).build()).queue();
            return;
        }
        mangaClient.search(query);
        messageToEdit.push(message);
    }

    @Override
    public void sendMangas(List<Manga> mangas) {
        if(mangas.isEmpty()) { messageToEdit.removeFirst().editMessageEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Could not find any results with that search query! Please try again with a valid manga!").build()).queue(); return; }
        messageToEdit.removeFirst().editMessageEmbeds(Tools.mangaToEmbed(mangas.get(0)).build()).queue();
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

    public void searchAnime(Message message, String query, boolean nsfw) {
        animeClient.searchAnime(query, nsfw);
        messageToEdit.push(message);
    }

    public void searchCharacter(Message message, String query) {
        animeClient.searchCharacter(query);
        messageToEdit.push(message);
    }

    public void randomQuote(Message message) {
        animeClient.randomQuote();
        messageToEdit.push(message);
    }

    public void getEpisodes(Message message, long id) {
        animeClient.getEpisodes(id);
        messageToEdit.push(message);
    }

    public void getNews(Message message, long id) {
        animeClient.getNews(id);
        messageToEdit.push(message);
    }

    public void getStatistics(ButtonInteractionEvent event, long id) {
        animeClient.getStatistics(id);
        eventToReply.push(event);
    }

    public void getThemes(ButtonInteractionEvent event, long id) {
        animeClient.getThemes(id);
        eventToReply.push(event);
    }

    public void getRecommendation(ButtonInteractionEvent event, long id) {
        animeClient.getRecommendation(id);
        eventToReply.push(event);
    }

    public void getReview(ButtonInteractionEvent event, long id) {
        animeClient.getReview(id);
        eventToReply.push(event);
    }

    public void getTop(Message message) {
        animeClient.getTop();
        messageToEdit.push(message);
    }

    public void getLatest(Message message) {
        animeClient.getLatest();
        messageToEdit.push(message);
    }

    public void randomAnime(Message message, boolean nsfw) {
        animeClient.randomAnime(nsfw);
        messageToEdit.push(message);
    }

    public void randomGIF(Message message, String type) {
        animeClient.randomGIF(type);
        messageToEdit.push(message);
    }

    @Override
    public void sendSearchAnime(ArrayList<Anime> animes) {
        Message message = messageToEdit.removeFirst();
        if(animes == null || animes.isEmpty()) return;
        AnimePage page = Search.messageToPage.get(message.getIdLong());
        page.timeout.cancel(false);
        page.animes = animes;
        animes.sort(Comparator.comparingInt(a -> {
            if(a.popularity < 1) return Integer.MAX_VALUE;
            return a.popularity;
        }));
        //this filters out 0 popularity search results rather than sending them to the back of the queue
        //animes = (ArrayList<Anime>) animes.stream().filter(a -> a.popularity > 0).collect(Collectors.toList());
        message.editMessageEmbeds(animes.get(0).toEmbed().setFooter("Page 1/" + animes.size()).build()).setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.primary("episodes", Emoji.fromUnicode("⏯")).withLabel("Episodes"),
                Button.primary("more", Emoji.fromUnicode("\uD83D\uDD0E")).withLabel("More Info"),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
        message.editMessageComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.primary("episodes", Emoji.fromUnicode("⏯")).withLabel("Episodes").asDisabled(),
                        Button.primary("more", Emoji.fromUnicode("\uD83D\uDD0E")).withLabel("More Info").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, me -> Search.messageToPage.remove(message.getIdLong()));
    }

    @Override
    public void sendSearchCharacter(Character character) {
        Message message = messageToEdit.removeFirst();
        message.editMessageEmbeds(character.toEmbed().build()).queue();
    }

    @Override
    public void sendRandomQuote(Quote quote) {
        Message message = messageToEdit.removeFirst();
        message.editMessageEmbeds(quote.toEmbed().build()).queue();
    }

    @Override
    public void sendEpisodes(ArrayList<Episode> episodes) {
        Message message = messageToEdit.removeFirst();
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

            Message m = message.editMessageEmbeds(
                    new EmbedBuilder()
                            .setTitle("Episode List")
                            .setDescription(pages.get(0))
                            .setFooter("Page 1/" + pages.size())
                            .build()
            ).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            AnimePage page = Search.messageToPage.get(message.getIdLong());
            page.episodes.get(page.pageNumber).pages = pages;
            Episodes.messageToPage.put(m.getIdLong(), page.episodes.get(page.pageNumber));
            m.editMessageComponents()
                    .queueAfter(3, TimeUnit.MINUTES, me -> Episodes.messageToPage.remove(m.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            message.editMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any episodes from that anime! Please try again later!")
                            .build()
            ).queue();
        }
    }

    @Override
    public void sendNews(ArrayList<Article> articles) {
        Message message = messageToEdit.removeFirst();
        try {
            if(articles == null || articles.isEmpty()) throw new Exception();
            Collections.reverse(articles);
            Message m = message.editMessageEmbeds(articles.get(0).toEmbed().setFooter("Page 1/" + articles.size()).build()).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            AnimePage page = Search.messageToPage.get(message.getIdLong());
            page.news.get(page.pageNumber).articles = articles;
            News.messageToPage.put(m.getIdLong(), page.news.get(page.pageNumber));
            m.editMessageComponents()
                    .queueAfter(3, TimeUnit.MINUTES, me -> News.messageToPage.remove(m.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            message.editMessageEmbeds(
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
        AnimePage page = Search.messageToPage.get(event.getMessageIdLong());
        page.stats.put(page.pageNumber, statistics.toEmbed().build());
    }

    @Override
    public void sendThemes(Themes themes) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(themes.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = Search.messageToPage.get(event.getMessageIdLong());
        page.themes.put(page.pageNumber, themes.toEmbed().build());
    }

    @Override
    public void sendRecommendation(Recommendation recommendation) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(recommendation.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = Search.messageToPage.get(event.getMessageIdLong());
        page.stats.put(page.pageNumber, recommendation.toEmbed().build());
    }

    @Override
    public void sendReview(Review review) {
        ButtonInteractionEvent event = eventToReply.removeFirst();
        event.replyEmbeds(review.toEmbed().build()).setEphemeral(true).queue();
        AnimePage page = Search.messageToPage.get(event.getMessageIdLong());
        page.review.put(page.pageNumber, review.toEmbed().build());
    }

    @Override
    public void sendTop(ArrayList<Anime> top) {
        Message message = messageToEdit.removeFirst();
        if(top == null) return;
        AnimePage page = Top.messageToPage.get(message.getIdLong());
        page.timeout.cancel(false);
        message.editMessageEmbeds(top.get(0).toEmbed().setFooter("Page 1/" + top.size()).build()).setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
        Top.messageToPage.get(message.getIdLong()).animes = top;
        message.editMessageComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, me -> Top.messageToPage.remove(message.getIdLong()));
    }

    @Override
    public void sendLatest(ArrayList<Anime> latest) {
        Message message = messageToEdit.removeFirst();
        if(latest == null) return;
        AnimePage page = Latest.messageToPage.get(message.getIdLong());
        page.timeout.cancel(false);
        message.editMessageEmbeds(latest.get(0).toEmbed().setFooter("Page 1/" + latest.size()).build()).setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
        Latest.messageToPage.get(message.getIdLong()).animes = latest;
        message.editMessageComponents()
                .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled()
                )
                .queueAfter(3, TimeUnit.MINUTES, me -> Latest.messageToPage.remove(message.getIdLong()));
    }

    @Override
    public void sendRandomAnime(Anime random) {
        Message message = messageToEdit.removeFirst();
        if(random == null) return;
        AnimePage page = RandomAnime.messageToPage.get(message.getIdLong());
        page.timeout.cancel(false);
        page.animes.add(random);
        message.editMessageEmbeds(random.toEmbed().build()).setActionRow(
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer")
        ).queue(msg -> msg.editMessageComponents()
                .queueAfter(3, TimeUnit.MINUTES, me -> RandomAnime.messageToPage.remove(message.getIdLong())));
    }

    @Override
    public void sendRandomGIF(GIF gif) {
        Message message = messageToEdit.removeFirst();
        if(gif == null) return;
        message.editMessageEmbeds(gif.toEmbed().build()).queue();
    }

}
