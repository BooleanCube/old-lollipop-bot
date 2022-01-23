package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Headbutt implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"headbutt"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Headbutt somebody\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "specified member", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://tenor.com/view/demon-slayer-kimetsu-no-yaiba-kamado-tanjiro-headbutt-gif-14914253", "https://tenor.com/view/tanjiro-kamado-kimetsu-no-yaiba-headbutt-calm-down-gif-14514262", "https://tenor.com/view/headbutt-fight-tanjiro-demon-slayer-protect-gif-14911019", "https://tenor.com/view/demonslayer-headbutt-fight-anime-gif-15640558", "https://tenor.com/view/dbz-gif-dragon-ball-z-hit-the-head-vegeta-gif-15112832", "https://tenor.com/view/anime-headbutt-anime-headbutt-princess-angry-gif-16519815"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        if(Objects.requireNonNull(event.getMember()).getIdLong() == target.getIdLong()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.reply("\uD83D\uDCAB **BANG** \uD83D\uDCAB \n" + target.getAsMention() + " was headbutted by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
