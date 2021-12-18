package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class Avatar implements Command {
    @Override
    public String getCommand() {
        return "avatar";
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    @Override
    public String getHelp() {
        return "Displays the mentioned user's avatar!\nUsage: `" + CONSTANT.PREFIX + getCommand() + " [user]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) {
            Member target = event.getMember();
            assert target != null;
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setImage(target.getEffectiveAvatarUrl() + "?size=512")
                    .setAuthor(target.getEffectiveName(), target.getAvatarUrl())
                    .build()
            ).queue();
        } else {
            Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
            if(target == null) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setImage(target.getEffectiveAvatarUrl() + "?size=512")
                    .setAuthor(target.getEffectiveName(), target.getAvatarUrl())
                    .build()
            ).queue();
        }
    }
}
