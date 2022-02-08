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

public class Ora implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"ora"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Ora Ora Ora Ora\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        String[] gifs = {"https://c.tenor.com/xZXvHLQWTP8AAAAC/jojo-star-platinum.gif", "https://c.tenor.com/LytxJSf81m4AAAAC/ora-beatdown-oraoraora.gif", "https://c.tenor.com/XQWVeTsLxWMAAAAC/jojos-bizarre-adventure-ora-ora.gif", "https://c.tenor.com/kXeAcAl6PmQAAAAS/star-platinum.gif", "https://c.tenor.com/Hf9MuA4oUrQAAAAS/star-platinum-ora.gif", "https://c.tenor.com/HzfPT3oEMMEAAAAC/star-platinum-the-world-jojo.gif"};
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
                .setDescription("**ORA ORA ORA ORA ORA ORA ORAAAAAAAA**\n" + target.getAsMention() + " was pulverized by " + event.getMember().getAsMention())
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }
}
