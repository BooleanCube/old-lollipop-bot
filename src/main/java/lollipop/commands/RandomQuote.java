package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.QuoteAPI;
import lollipop.Tools;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public class RandomQuote implements Command {
    @Override
    public String getCommand() {
        return "quote";
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Throws back a random anime related quote at you!\nUsage: `" + CONSTANT.PREFIX + getCommand() + "`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(!args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        try { QuoteAPI.sendQuote(event.getChannel()); } catch (IOException e) {}
    }
}
