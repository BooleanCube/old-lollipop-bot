package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Objects;

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
        return "Calls somebody a baka!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://c.tenor.com/G4zCaHnNxysAAAAC/anime-boy-baka-baka.gif", "https://tenor.com/view/baka-anime-gif-12908346", "https://tenor.com/view/baka-anime-gif-22001672", "https://tenor.com/view/baka-gif-19268094", "https://tenor.com/view/sasuke-naruto-anime-mad-baka-gif-17737654", "https://tenor.com/view/anime-fiduka-no-baka-blah-gif-12414850"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        if(Objects.requireNonNull(event.getMember()).getIdLong() == target.getIdLong()) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.getChannel().sendMessage("**Bakana no?**\n" + target.getAsMention() + " was called a **baka** by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
