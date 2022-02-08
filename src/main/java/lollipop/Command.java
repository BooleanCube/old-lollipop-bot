package lollipop;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface Command {
    String[] getAliases();
    String getCategory();
    String getHelp();
    CommandData getSlashCmd();
    void run(SlashCommandInteractionEvent event);
}