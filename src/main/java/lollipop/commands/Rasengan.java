package lollipop.commands;

import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

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
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://tenor.com/view/naruto-rasengan-anime-running-gif-16159006", "https://tenor.com/view/naruto-anime-rasengan-gif-11805675", "https://tenor.com/view/naruto-rasengan-clone-anime-gif-7239436", "https://tenor.com/view/naruto-rasengan-gif-8212746", "https://tenor.com/view/naruto-rasengan-clones-naruto-shippuden-power-gif-15111421", "https://tenor.com/view/rasengan-minato-obito-uchiha-anime-gif-12425387"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        event.getChannel().sendMessage("**RASENGAN!**\n" + target.getAsMention() + " was blasted away by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
