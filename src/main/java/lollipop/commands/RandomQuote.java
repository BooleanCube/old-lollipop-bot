package lollipop.commands;

import lollipop.API;
import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RandomQuote implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"quote"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Throws back a random anime related quote at you!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        try { API.sendQuote(event.getChannel()); } catch (IOException e) {}
    }
}
