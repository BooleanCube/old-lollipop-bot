package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class Eat implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"eat", "food", "nom"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "*nom nom nom*\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(!args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://tenor.com/view/inazuma-eleven-ina11-eating-onigiri-rice-ball-gif-19866294", "https://tenor.com/view/pokemon-eat-eating-yum-food-gif-8995781", "https://tenor.com/view/cute-girl-eating-onigiri-neko-ears-purple-hair-purple-eyes-gif-20674089", "https://tenor.com/view/anime-sailor-moon-eat-eating-food-gif-12390158", "https://tenor.com/view/cutie-honey-hiroyuki-imaishi-go-nagai-adorable-anime-gif-16787211", "https://tenor.com/view/anime-rice-riceball-food-eating-gif-12390145", "https://tenor.com/view/spirited-away-chihiro-naku-gif-11153707"};
        event.getChannel().sendMessage("*nom nom nom nom nom*\n" + event.getMember().getAsMention() + " is eating!").queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
