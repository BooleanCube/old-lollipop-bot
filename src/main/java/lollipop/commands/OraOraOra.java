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

public class OraOraOra implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"ora"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Ora Ora Ora Ora\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] oragifs = {"https://tenor.com/view/jojo-star-platinum-ora-ora-gif-15668236", "https://tenor.com/view/ora-beatdown-oraoraora-ora-gif-22314453", "https://tenor.com/view/jojos-bizarre-adventure-ora-ora-gif-14649361", "https://tenor.com/view/star-platinum-gif-18007371", "https://tenor.com/view/star-platinum-ora-jjba-jo-jos-bizarre-adventure-anime-gif-17783790", "https://tenor.com/view/star-platinum-the-world-jojo-jjba-anime-jojos-bizarre-adventure-gif-17132041"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        if(Objects.requireNonNull(event.getMember()).getIdLong() == target.getIdLong()) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.getChannel().sendMessage("**ORA ORA ORA ORA ORA ORA ORAAAAAAAA**\n" + target.getAsMention() + " was pounded by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(oragifs[(int)(Math.random()*oragifs.length)]).queue();
    }
}
