package lollipop.commands;

import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;
import java.util.stream.Collectors;

public class Ping implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"ping"};
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    @Override
    public String getHelp() {
        return "Gives you the gateway and rest ping of the bot.\n" +
                "Usage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Ping!")
                .addField("Gateway Ping", event.getJDA().getGatewayPing() + "ms", true)
                .addField("Rest Ping", event.getJDA().getRestPing().complete() + "ms", true)
                .build()
        ).queue();
    }

}