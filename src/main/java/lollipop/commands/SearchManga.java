package lollipop.commands;

import lollipop.API;
import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class SearchManga implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"searchmanga", "searchm", "sm"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Searches for a manga with the given search query!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [query]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.STRING, "query", "search manga name", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        API api = new API();
        String query = String.join(" ", args);
        Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
        api.searchMangas(query, msg);
    }

}
