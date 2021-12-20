package lollipop;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public interface Command {
    String[] getAliases();
    String getCategory();
    String getHelp();
    void run(List<String> args, MessageReceivedEvent event);
}