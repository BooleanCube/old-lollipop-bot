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

public class Punch implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"punch"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Punch somebody!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        String[] gifs = {"https://c.tenor.com/--80HfIQWT4AAAAd/punchy-one-punch-man.gif", "https://c.tenor.com/1Sd82w25kacAAAAC/one-punch-man-punch.gif", "https://c.tenor.com/DYCMu6qWQ6YAAAAC/si.gif", "https://c.tenor.com/cn31gxGtMJwAAAAC/one-punch-man-saitama.gif", "https://c.tenor.com/j38Y2WWBJiUAAAAd/one-punch-man.gif"};
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
                .setDescription("**Anybody in my way... gets punched.**\n" + target.getAsMention() + " was punched by " + event.getMember().getAsMention())
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }
}
