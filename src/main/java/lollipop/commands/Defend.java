package lollipop.commands;

import lollipop.Command;
import lollipop.Constant;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;

public class Defend implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"defend"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Defend yourself from an attack!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        String[] gifs = new String[]{"https://c.tenor.com/aq0i3wbHc08AAAAC/dagger-anime.gif", "https://c.tenor.com/TPaJW2RZyIYAAAAC/anime-dodge.gif", "https://c.tenor.com/_RiEmuK5xS8AAAAd/garou-defend.gif", "https://c.tenor.com/WO8INqwCItcAAAAC/dragon-ball-anime.gif", "https://c.tenor.com/ObXEEfb_m1cAAAAC/goten-dbz.gif", "https://c.tenor.com/Z8Q13fWVtQkAAAAC/block-deflect.gif"};
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("\uD83D\uDE46 *block* \uD83D\uDE45 \n" + event.getUser().getAsMention() + " defended themselves!")
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }

}
