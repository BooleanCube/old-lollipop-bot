package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.MangaAPI;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class SearchManga implements Command {

    @Override
    public String getCommand() {
        return "searchm";
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Searches for a manga with the given arguments!\nUsage: `" + CONSTANT.PREFIX + getCommand() + " [query]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        MangaAPI api = new MangaAPI();
        String query = String.join(" ", args);
        Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
        api.searchMangas(query, msg);
    }

}
