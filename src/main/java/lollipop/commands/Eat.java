package lollipop.commands;

import lollipop.CommandType;
import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class Eat implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"eat", "food", "nom"};
    }

    @Override
    public CommandType getCategory() {
        return CommandType.ROLEPLAY;
    }

    @Override
    public String getHelp() {
        return "*nom nom nom*\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String[] gifs = {"https://c.tenor.com/T6jB8_AfdwsAAAAC/inazuma-eleven-ina11.gif", "https://c.tenor.com/KJQ_GeUPtJcAAAAC/pokemon-eat.gif", "https://c.tenor.com/r_zzF127q5IAAAAC/cute-girl-eating-onigiri.gif", "https://c.tenor.com/wHrdMy11-h8AAAAC/anime-sailor-moon.gif", "https://c.tenor.com/Q5PWpPOZ6xMAAAAC/cutie-honey-hiroyuki-imaishi.gif", "https://c.tenor.com/NrW4P5Xv9UQAAAAC/anime-rice.gif", "https://c.tenor.com/JEdcT8vhffgAAAAC/spirited-away-chihiro.gif"};
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("*nom nom nom nom nom*\n" + event.getUser().getAsMention() + " is eating!")
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }

}
