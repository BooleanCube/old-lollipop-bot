package lollipop.commands;

import awatch.models.Anime;
import lollipop.*;
import lollipop.models.AnimePage;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import java.io.IOException;
import java.util.HashMap;

public class Random implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"random", "r"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Get a random anime!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public void run(SlashCommandInteractionEvent event) {
        API api = new API();
        try {
            Anime a = api.randomAnime(event.getTextChannel().isNSFW());
            InteractionHook msg = event.replyEmbeds(Tools.animeToEmbed(a).build())
                    .addActionRow(
                            Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer")
                    ).complete();
            Message message = msg.retrieveOriginal().complete();
            messageToPage.put(message.getIdLong(), new AnimePage(a, message, event.getUser()));
        } catch(IOException ignored) {}
    }
}
