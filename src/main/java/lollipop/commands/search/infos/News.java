package lollipop.commands.search.infos;

import awatch.model.Article;
import lollipop.*;
import lollipop.pages.AnimePage;
import lollipop.pages.Newspaper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * News Model for search command
 */
public class News {

    static API api = new API();
    public static HashMap<Long, Newspaper> messageToPage = new HashMap<>();

    /**
     * Runs a request to get anime news
     * @param event button interaction event
     * @param page anime page
     */
    public static void run(SelectMenuInteractionEvent event, AnimePage page) {
        api.getNews(event, page);
    }

}