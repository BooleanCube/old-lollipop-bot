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

public class InfiniteVoid implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"infinitevoid"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Domain Expansion: Infinite Void!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "specified member", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        String[] gifs = {"https://c.tenor.com/84Y17eI-b0oAAAAS/infinite-void-gojo.gif", "https://c.tenor.com/MfMS51gTUc4AAAAS/satoru-gojo-domain-expansion.gif", "https://c.tenor.com/LOrTA4poJjEAAAAS/gojo-blindfold.gif", "https://c.tenor.com/CrX9wY8ibikAAAAS/sawunn.gif", "https://c.tenor.com/Tt0MU3RgnoQAAAAd/jjk-gojo.gif", "https://c.tenor.com/QM3oFaOSQIgAAAAS/jujustu-kaisen-satoru-gojo.gif", "https://c.tenor.com/AydW1nG8SpoAAAAC/anime.gif"};
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
                .setDescription("**Domain Expansion: Infinite Void**\n" + target.getAsMention() + " is stuck in the infinite void casted by " + event.getMember().getAsMention())
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }
}
