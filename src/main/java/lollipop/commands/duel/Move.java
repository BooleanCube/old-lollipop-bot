package lollipop.commands.duel;

import lollipop.Command;
import lollipop.Tools;
import lollipop.commands.duel.models.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

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
        return "Gives detail about a specific move that is available in a duel!";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.size() >= 1) {
            if(args.size() == 1 && args.get(0).equalsIgnoreCase("all")) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setDescription(Game.getAvailableMoves())
                        .build()
                ).queue();
                return;
            }
            String moveDesc = Game.moveDescription(String.join(" ", args));
            if(moveDesc == null) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setDescription("I could not find an available move under that name! Please try again with a different input or do `l!move all` to get a list of all the available moves!")
                        .setColor(Color.red)
                        .build()
                ).queue();
            } else {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setDescription(moveDesc)
                        .build()
                ).queue();
            }
        } else Tools.wrongUsage(event.getTextChannel(), this);
    }
}
