package lollipop.commands;

import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;

public class Baka implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"baka"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Call somebody a baka!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        if(options.isEmpty()) { Tools.wrongUsage(event, this); return; }
        String[] gifs = {"https://c.tenor.com/G4zCaHnNxysAAAAC/anime-boy-baka-baka.gif", "https://c.tenor.com/Xcr8fHyf84gAAAAC/baka-anime.gif", "https://c.tenor.com/UsggMuRixo0AAAAC/baka-anime.gif", "https://c.tenor.com/5GLN3NF8IawAAAAC/baka.gif", "https://c.tenor.com/rSW3Ty17towAAAAC/sasuke-naruto.gif", "https://c.tenor.com/OyIYV1OjcjQAAAAC/anime-fiduka.gif"};
        User target = options.get(0).getAsUser();
        if(event.getUser().getIdLong() == target.getIdLong()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("**Bakana no?**\n" + target.getAsMention() + " was called a **baka** by " + event.getUser().getAsMention())
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }

}
