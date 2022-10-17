package lollipop;

import lollipop.commands.*;
import lollipop.commands.duel.Duel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    public Collection<Command> getCommands(CommandType category) {
        ArrayList<Command> r = new ArrayList<>();
        List<Command> values = commands.values().stream().filter(c -> c.getCategory() == category).collect(Collectors.toList());
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

    void run(SlashCommandInteractionEvent event) {
        final String msg = event.getCommandString();
        if(!event.getGuild().getSelfMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_SEND) &&
                !event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) return;
        final String command = event.getName();
        if (commands.containsKey(command)) {
            if(event.getUser().isBot()) {
                event.reply("Nice try, you lowly peasant! Only my masters can command me!")
                        .queue(m -> m.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            commands.get(command).run(event);
        }
    }

}
