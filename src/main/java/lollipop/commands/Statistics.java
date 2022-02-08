package lollipop.commands;

import lollipop.API;
import lollipop.Command;
import lollipop.Constant;
import lollipop.Tools;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.IOException;
import java.util.List;

public class Statistics implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"stats", "statistics"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Returns Statistical Information about an anime!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [id]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.INTEGER, "id", "MAL ID (available in the search command)", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        API api = new API();
        long id = options.get(0).getAsLong();
        try {
            event.replyEmbeds(Tools.statsToEmbed(api.getAnimeStats(id)).build()).queue();
        } catch (IOException ignored) {
            Tools.wrongUsage(event, this);
        }
    }
}
