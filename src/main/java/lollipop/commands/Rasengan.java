package lollipop.commands;

import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Rasengan implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"rasengan"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Rasengan!";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        String[] gifs = {"https://c.tenor.com/VXJeIRgdC_oAAAAC/naruto-rasengan.gif", "https://c.tenor.com/owUrJK4G2CoAAAAC/naruto-anime.gif", "https://c.tenor.com/LZlkPMGI9mMAAAAC/naruto-rasengan.gif", "https://c.tenor.com/sWqQhy0AExoAAAAC/naruto-rasengan.gif", "https://c.tenor.com/4FFD2H294mAAAAAC/naruto-rasengan.gif", "https://c.tenor.com/YdgIUNRqy3wAAAAC/rasengan-minato.gif"};
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
                .setDescription("**RASENGAN!**\n" + target.getAsMention() + " was blasted away by " + event.getMember().getAsMention())
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }
}
