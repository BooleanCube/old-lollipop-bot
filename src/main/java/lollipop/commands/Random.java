package lollipop.commands;

import awatch.models.Anime;
import lollipop.*;
import lollipop.models.AnimePage;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
        return "Get a random anime!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + "`";
    }

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        API api = new API();
        if(args.isEmpty()) try {
            Anime a = api.randomAnime();
            Message msg = event.getChannel().sendMessageEmbeds(Tools.animeToEmbed(a).build())
                    .setActionRow(Button.primary("trailer", Emoji.fromUnicode("▶")).withLabel("Trailer"))
                    .complete();
            messageToPage.put(msg.getIdLong(), new AnimePage(a, msg, event.getAuthor()));
        } catch(IOException ignored) {}
        else Tools.wrongUsage(event.getTextChannel(), this);
    }
}
