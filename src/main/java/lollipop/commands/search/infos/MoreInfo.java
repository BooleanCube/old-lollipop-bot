package lollipop.commands.search.infos;

import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MoreInfo {

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    public static void run(ButtonInteractionEvent event, AnimePage page) {
        InteractionHook m = event.replyEmbeds(
                new EmbedBuilder()
                        .setTitle("Additional Information:")
                        .setFooter("The following information is displayed for the anime selected in the embed page above!")
                        .build()
        ).addActionRow(
                Button.primary("theme", Emoji.fromUnicode("\uD83C\uDFB6")).withLabel("Themes"),
                Button.primary("review", Emoji.fromUnicode("\uD83D\uDD22")).withLabel("Review"),
                Button.primary("news", Emoji.fromUnicode("\uD83D\uDCF0")).withLabel("News"),
                Button.primary("stats", Emoji.fromUnicode("\uD83D\uDCCA")).withLabel("Statistics"),
                Button.primary("recommendations", Emoji.fromUnicode("⏏")).withLabel("Recommendations")
        ).complete();
        Message msg = m.retrieveOriginal().complete();
        m.editOriginalComponents().setActionRow(
                Button.primary("theme", Emoji.fromUnicode("\uD83C\uDFB6")).withLabel("Themes").asDisabled(),
                Button.primary("review", Emoji.fromUnicode("\uD83D\uDD22")).withLabel("Review").asDisabled(),
                Button.primary("news", Emoji.fromUnicode("\uD83D\uDCF0")).withLabel("News").asDisabled(),
                Button.primary("stats", Emoji.fromUnicode("\uD83D\uDCCA")).withLabel("Statistics").asDisabled(),
                Button.primary("recommendations", Emoji.fromUnicode("⏏")).withLabel("Recommendations").asDisabled()
        ).queueAfter(3, TimeUnit.MINUTES, me->messageToPage.remove(msg.getIdLong()));
        messageToPage.put(msg.getIdLong(), page);
        event.getMessage().editMessageComponents().setActionRow(
                Button.secondary("left", Emoji.fromUnicode("⬅")),
                Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"),
                Button.primary("episodes", Emoji.fromUnicode("⏯")).withLabel("Episodes"),
                Button.primary("more", Emoji.fromUnicode("\uD83D\uDD0E")).withLabel("More Info").asDisabled(),
                Button.secondary("right", Emoji.fromUnicode("➡"))
        ).complete();
    }

}
