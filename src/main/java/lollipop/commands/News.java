package lollipop.commands;

import awatch.models.Article;
import lollipop.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class News implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"news", "n"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Returns the latest news on a given anime!\n*Note: You must pass in the `ID` instead of a name. The `ID` can be located at the top of a `search` command.*\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [ID]`";
    }

    public static HashMap<Long, Newspaper> messageToPage = new HashMap<>();

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.size()<1) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        AnimeAPI api = new AnimeAPI();
        long id = 0;
        try { id = Long.parseLong(args.get(0)); }
        catch(Exception e) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for news...").build()).complete();
        try {
            ArrayList<Article> articles = api.animeNews(id);
            if(articles == null) throw new Exception();
            Collections.reverse(articles);
            Message m = msg.editMessageEmbeds(Tools.newsEmbed(articles.get(0)).setFooter("Page 1/" + articles.size()).build()).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            messageToPage.put(m.getIdLong(), new Newspaper(articles, 1, m, event.getAuthor()));

            m.editMessageComponents()
                    .setActionRow(
                        Button.secondary("left", Emoji.fromUnicode("⬅")).asDisabled(),
                        Button.secondary("right", Emoji.fromUnicode("➡")).asDisabled(),
                        Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer").asDisabled()
                    )
                    .queueAfter(3, TimeUnit.MINUTES, me -> messageToPage.remove(m.getIdLong()));
        }
        catch (Exception e) {
            msg.editMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find an anime with that search query! Please try again with a valid anime!")
                            .build()
            ).queue();
        }
    }
}
