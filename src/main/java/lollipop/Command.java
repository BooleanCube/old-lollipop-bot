package lollipop;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface Command {

    /**
     * Aliases for the command
     * @return alias list - string[]
     */
    String[] getAliases();

    /**
     * Get command category for help command
     * @return command category - string
     */
    String getCategory();

    /**
     * Get command description and help
     * @return command help - string
     */
    String getHelp();

    /**
     * Get slash command data
     * @return slash command data - CommandData
     */
    CommandData getSlashCmd();

    /**
     * Runs when the command is called
     * @param event slash command interaction event
     */
    void run(SlashCommandInteractionEvent event);

}