package lollipop.commands;

import lollipop.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
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
    public CommandType getCategory() {
        return CommandType.ANIME;
    }

    @Override
    public String getHelp() {
        return "Throws back a random anime related quote at you!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    public API api;

    public RandomQuote() {
        this.api = new API();
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        InteractionHook message = event.replyEmbeds(
                new EmbedBuilder()
                        .setDescription("Getting a random `quote`...")
                        .build()
        ).complete();
        api.randomQuote(message);
    }

}
