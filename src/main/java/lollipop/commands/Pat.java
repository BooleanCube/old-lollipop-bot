package lollipop.commands;

import lollipop.CommandType;
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

public class Pat implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"pat"};
    }

    @Override
    public CommandType getCategory() {
        return CommandType.ROLEPLAY;
    }

    @Override
    public String getHelp() {
        return "Pat somebody!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        String[] gifs = {"https://c.tenor.com/OGnRVWCps7IAAAAC/anime-head-pat.gif", "https://c.tenor.com/N41zKEDABuUAAAAC/anime-head-pat-anime-pat.gif", "https://c.tenor.com/wLqFGYigJuIAAAAC/mai-sakurajima.gif", "https://c.tenor.com/8DaE6qzF0DwAAAAC/neet-anime.gif", "https://c.tenor.com/E6fMkQRZBdIAAAAC/kanna-kamui-pat.gif", "https://c.tenor.com/rZRQ6gSf128AAAAC/anime-good-girl.gif", "https://c.tenor.com/6dLDH0npv6IAAAAC/nogamenolife-shiro.gif", "https://c.tenor.com/jEfC8cztigIAAAAC/anime-pat.gif"};
        User target = options.get(0).getAsUser();
        if(event.getUser().getIdLong() == target.getIdLong()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("*pat pat pat pat*\n" + target.getAsMention() + " was patted by " + event.getUser().getAsMention())
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }

}
