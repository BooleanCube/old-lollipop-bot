package lollipop;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface Command {

    /**
     * Aliases for the command
     * @return String[] for the alias list
     * (no longer needed to be a string[] after the slash command implementation)
     */
    String[] getAliases();

    /**
     * Get command category for help command
     * @return String for the command category
     */
    String getCategory();

    /**
     * Get command description and help
     * @return String for the command help
     */
    String getHelp();

    /**
     * Get slash command data
     * @return {@link CommandData} with all the slash command data
     */
    CommandData getSlashCmd();

    /**
     * Runs when the command is called
     * @param event slash command interaction event
     */
    void run(SlashCommandInteractionEvent event);

}