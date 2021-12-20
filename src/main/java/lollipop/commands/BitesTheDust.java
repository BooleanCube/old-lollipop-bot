package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BitesTheDust implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"biteszadust"};
    }

    @Override
    public String getCategory() {
        return "Utility";
    }

    @Override
    public String getHelp() {
        return "Deletes your recent messages in a channel!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(!args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        event.getMessage().delete().complete();
        List<Message> msgList = event.getChannel().getHistory().retrievePast(31).complete().stream().filter(m -> m.getMember().getIdLong() == event.getMember().getIdLong()).collect(Collectors.toList());
        try {
            if(msgList.isEmpty()) throw new Exception();
            event.getChannel().purgeMessages(msgList);
            event.getChannel().sendMessage("Successfully travelled back 30 messages in time without leaving any traces behind!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
        } catch(Exception e) {
            event.getChannel().sendMessage("You haven't done anything recently to travel back in time!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
        }
    }
}
