package lollipop;

import lollipop.commands.*;
import lollipop.commands.Random;
import lollipop.commands.duel.Duel;
import lollipop.commands.duel.Move;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Manager {

    private final Map<String, Command> commands = new HashMap<>();

    Manager() {
        //commands
        addCommand(new Help(this));
        addCommand(new Gif());
        addCommand(new Ping());
        addCommand(new Search());
        addCommand(new Picture());
        addCommand(new BotInfo());
        addCommand(new Avatar());
        addCommand(new Eval());
        addCommand(new StatisticsInfo());
        addCommand(new OraOraOra());
        addCommand(new Janken());
        addCommand(new Hentai());
        addCommand(new Baka());
        addCommand(new RandomQuote());
        addCommand(new BitesTheDust());
        addCommand(new Pat());
        addCommand(new Rasengan());
        addCommand(new Onigiri());
        addCommand(new Eat());
        addCommand(new Hinokami());
        addCommand(new InfiniteVoid());
        addCommand(new Headbutt());
        addCommand(new News());
        addCommand(new Random());
        addCommand(new Top());
        addCommand(new Punch());
        addCommand(new Duel());
        addCommand(new Move());
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
        if(!event.getGuild().getSelfMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_SEND) &&
           !event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) return;
        final String[] split = msg.replaceFirst("(?i)" + Pattern.quote(CONSTANT.PREFIX), "").split("\\s+");
        final String command = split[0].toLowerCase();
        if (commands.containsKey(command)) {
            if(Objects.requireNonNull(event.getMember()).getUser().isBot()) {
                event.getChannel().sendMessage("Nice try, you lowly peasant! Only my masters can command me!")
                        .queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            commands.get(command).run(args, event);
        }
    }

}
