package lollipop.commands;

import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return "Headbutt somebody\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "specified member", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        String[] gifs = {"https://c.tenor.com/1xHAx5bI1DQAAAAC/demon-slayer-kimetsu-no-yaiba.gif", "https://c.tenor.com/SSgZ9SP_NeAAAAAC/tanjiro-kamado-kimetsu-no-yaiba.gif", "https://c.tenor.com/bRvQowf8RfkAAAAC/headbutt-fight.gif", "https://c.tenor.com/4AvIBPKxbOwAAAAS/demonslayer-headbutt.gif", "https://c.tenor.com/XhsmfZrj8EMAAAAC/dbz-gif.gif", "https://c.tenor.com/0zAC3rdS_5kAAAAC/anime-headbutt.gif"};
        Member target = options.get(0).getAsMember();
        if(target == null) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        if(Objects.requireNonNull(event.getMember()).getIdLong() == target.getIdLong()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.replyEmbeds(new EmbedBuilder()
                        .setDescription("\uD83D\uDCAB **BANG** \uD83D\uDCAB \n" + target.getAsMention() + " was headbutted by " + event.getMember().getAsMention())
                        .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }
}
