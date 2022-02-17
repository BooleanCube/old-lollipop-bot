package lollipop.commands.duel;

import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import lollipop.commands.duel.models.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Move implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"move"};
    }

    @Override
    public String getCategory() {
        return "Fun";
    }

    @Override
    public String getHelp() {
        return "Gives detail about a specific move that is available in a duel!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [move*]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.STRING, "move", "available move name", false);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        if(args.size() >= 1) {
            if(args.size() == 1 && args.get(0).equalsIgnoreCase("all")) {
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription(Game.getAvailableMoves())
                        .build()
                ).queue();
                return;
            }
            String moveDesc = Game.moveDescription(String.join(" ", args));
            if(moveDesc == null) {
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("I could not find an available move under that name! Please try again with a different input or do `" + Constant.PREFIX + "move all` to get a list of all the available moves!")
                        .setColor(Color.red)
                        .build()
                ).queue();
            } else {
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription(moveDesc)
                        .build()
                ).queue();
            }
        } else {
            event.replyEmbeds(new EmbedBuilder()
                    .setDescription(Game.getAvailableMoves())
                    .build()
            ).queue();
        }
    }
}
