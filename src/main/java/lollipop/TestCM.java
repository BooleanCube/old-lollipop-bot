package lollipop;

import lollipop.commands.*;
import lollipop.commands.duel.Duel;
import lollipop.commands.duel.Move;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestCM {

    private final Map<String, Command> commands = new HashMap<>();

    TestCM() {
        //new commands
        addCommand(new Ping());
        addCommand(new Duel());
    }

    private void addCommand(Command c) {
        if (!commands.containsKey(c.getAliases()[0])) for(String cmd : c.getAliases()) commands.put(cmd, c);
    }

    public Collection<Command> getCommands(String category) {
        ArrayList<Command> r = new ArrayList<>();
        List<Command> values = commands.values().stream().filter(c -> c.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
        for(Command c : values) if(!r.contains(c)) r.add(c);
        return r;
    }

    public Collection<Command> getCommands() {
        ArrayList<Command> r = new ArrayList<>();
        List<Command> values = new ArrayList<>(commands.values());
        for(Command c : values) if(!r.contains(c)) r.add(c);
        return r;
    }

    public Command getCommand(String commandName) {
        if (commandName == null) return null;
        return commands.get(commandName);
    }

    void run(MessageReceivedEvent event) {
        final String msg = event.getMessage().getContentRaw();
        if (!msg.startsWith(CONSTANT.PREFIX)) return;
        final String[] split = msg.replaceFirst("(?i)" + Pattern.quote(CONSTANT.PREFIX), "").split("\\s+");
        final String command = split[0].toLowerCase();
        if (commands.containsKey(command)) {
            if(event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("Nice try, you lowly peasant! Only my masters can command me!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            commands.get(command).run(args, event);
        }
    }

}
