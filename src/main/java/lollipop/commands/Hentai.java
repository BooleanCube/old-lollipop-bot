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

public class Hentai implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"hentai"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Calls somebody a Hentai!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://c.tenor.com/MifS9QJUGA4AAAAC/anime-angry.gif", "https://c.tenor.com/-85WiDA6074AAAAC/anime-girl.gif", "https://c.tenor.com/MLsVzlSceaEAAAAC/anime-angry.gif", "https://c.tenor.com/oxqylurVQmkAAAAC/touken-angry.gif", "https://c.tenor.com/0P7u23ALUC0AAAAC/yandere-test-why-comfy-black-hair-anime.gif", "https://c.tenor.com/_G8gPkGWLPEAAAAC/anime-yelling.gif"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        if(Objects.requireNonNull(event.getMember()).getIdLong() == target.getIdLong()) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.getChannel().sendMessage("**HENTAAAAIIIIIIIIIIIIIIIIIIIIIII**\n" + target.getAsMention() + " was called a **hentai** by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
