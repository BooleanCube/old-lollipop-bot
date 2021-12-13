package lollipop.commands;

import lollipop.AnimeAPI;
import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public class SearchAnime implements Command {
    @Override
    public String getCommand() {
        return "searcha";
    }

    @Override
    public String getHelp() {
        return "This command finds an anime with the given search query!\nUsage: `" + CONSTANT.PREFIX + getCommand() + " [query]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        AnimeAPI api = new AnimeAPI();
        try {
            String query = String.join(" ", args);
            Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
            msg.editMessageEmbeds(Tools.animeToEmbed(api.searchForAnime(query)).build()).queue(); }
        catch (IOException e) {}
    }
}

