package lollipop.commands.search.infos;

import awatch.models.Article;
import lollipop.*;
import lollipop.pages.AnimePage;
import lollipop.pages.Newspaper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class News {

    public static HashMap<Long, Newspaper> messageToPage = new HashMap<>();

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        API api = new API();
        long id = page.animes.get(page.pageNumber-1).malID;
        InteractionHook msg = event.replyEmbeds(
                new EmbedBuilder()
                        .setDescription("Searching for news...")
                        .build()
        ).setEphemeral(true).complete();
        try {
            ArrayList<Article> articles = api.animeNews(id);
            if(articles == null || articles.isEmpty()) throw new Exception();
            Collections.reverse(articles);
            Message m = msg.editOriginalEmbeds(Tools.newsEmbed(articles.get(0)).setFooter("Page 1/" + articles.size()).build()).setActionRow(
                    Button.secondary("left", Emoji.fromUnicode("⬅")),
                    Button.secondary("right", Emoji.fromUnicode("➡"))
            ).complete();
            page.news.put(page.pageNumber, new Newspaper(articles, 1, m, event.getUser()));
            messageToPage.put(m.getIdLong(), page.news.get(page.pageNumber));
            m.editMessageComponents()
                    .queueAfter(3, TimeUnit.MINUTES, me -> messageToPage.remove(m.getIdLong()));
        }
        catch (Exception e) {
            e.printStackTrace();
            msg.editOriginalEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("Could not find any articles from that anime! Please try again later!")
                            .build()
            ).queue();
        }
    }

}