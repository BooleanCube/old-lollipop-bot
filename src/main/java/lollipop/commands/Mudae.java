package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class Mudae implements Command {
    @Override
    public String getCommand() {
        return "mudae";
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Mudae Mudae Mudae\nUsage: `" + CONSTANT.PREFIX + getCommand() + " [user]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] mudaegifs = {"https://tenor.com/view/jojo-jojos-bizarre-adventures-ora-ora-ora-the-world-stand-gif-15566871", "https://tenor.com/view/jojo-jotaro-dio-brando-kujo-gif-21583125", "https://tenor.com/view/dio-jojo-jjba-anime-fire-gif-16677683"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        event.getChannel().sendMessage("**MUDAE MUDAE MUDAE MUDAAAEEEEEEE**\n" + target.getAsMention() + " was powdered by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(mudaegifs[(int)(Math.random()*mudaegifs.length)]).queue();
    }
}
