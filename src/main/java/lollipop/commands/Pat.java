package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class Pat implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"pat"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Pat somebody!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://tenor.com/view/anime-head-pat-gif-23472603", "https://tenor.com/view/anime-head-pat-anime-pat-pat-anime-blush-gif-23411369", "https://tenor.com/view/mai-sakurajima-sakuta-anime-pat-gif-22595893", "https://tenor.com/view/neet-anime-cute-kawaii-pat-gif-9332926", "https://tenor.com/view/kanna-kamui-pat-head-pat-gif-12018819", "https://tenor.com/view/anime-good-girl-pet-pat-gif-9200932", "https://tenor.com/view/nogamenolife-shiro-headrub-sleepy-tired-gif-6238142", "https://tenor.com/view/anime-pat-smile-cute-blush-gif-16456868"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        event.getChannel().sendMessage("*pat pat pat pat*\n" + target.getAsMention() + " was patted by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
