package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;

public class Avatar implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"avatar"};
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    @Override
    public String getHelp() {
        return "Displays the mentioned user's avatar!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "specified member", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.isEmpty()) {
            Member target = event.getMember();
            assert target != null;
            event.replyEmbeds(new EmbedBuilder()
                    .setImage(target.getEffectiveAvatarUrl() + "?size=512")
                    .setAuthor(target.getEffectiveName(), target.getAvatarUrl())
                    .build()
            ).queue();
        } else {
            Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
            if(target == null) {
                event.replyEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
                return;
            }
            event.replyEmbeds(new EmbedBuilder()
                    .setImage(target.getEffectiveAvatarUrl() + "?size=512")
                    .setAuthor(target.getEffectiveName(), target.getAvatarUrl())
                    .build()
            ).queue();
        }
    }
}
