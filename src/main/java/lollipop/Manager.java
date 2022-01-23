package lollipop;

import lollipop.commands.Random;
import lollipop.commands.*;
import lollipop.commands.duel.Duel;
import lollipop.commands.duel.Move;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Manager {

    private final Map<String, Command> commands = new HashMap<>();

    public Manager() {
        setCommands();
    }

    public void reloadCommands(JDA jda) {
        //delete all current slash commands
        jda.updateCommands().queue();
        //add all slash commands
        jda.updateCommands()
                .addCommands(new Help(this).getSlashCmd())
                .addCommands(new Gif().getSlashCmd())
                .addCommands(new Ping().getSlashCmd())
                .addCommands(new Search().getSlashCmd())
                .addCommands(new Picture().getSlashCmd())
                .addCommands(new BotInfo().getSlashCmd())
                .addCommands(new Avatar().getSlashCmd())
                .addCommands(new Eval().getSlashCmd())
                .addCommands(new StatisticsInfo().getSlashCmd())
                .addCommands(new OraOraOra().getSlashCmd())
                .addCommands(new Janken().getSlashCmd())
                .addCommands(new Hentai().getSlashCmd())
                .addCommands(new Baka().getSlashCmd())
                .addCommands(new RandomQuote().getSlashCmd())
                .addCommands(new BitesTheDust().getSlashCmd())
                .addCommands(new Pat().getSlashCmd())
                .addCommands(new Rasengan().getSlashCmd())
                .addCommands(new Onigiri().getSlashCmd())
                .addCommands(new Eat().getSlashCmd())
                .addCommands(new Hinokami().getSlashCmd())
                .addCommands(new InfiniteVoid().getSlashCmd())
                .addCommands(new Headbutt().getSlashCmd())
                .addCommands(new News().getSlashCmd())
                .addCommands(new Random().getSlashCmd())
                .addCommands(new Top().getSlashCmd())
                .addCommands(new Punch().getSlashCmd())
                .addCommands(new Duel().getSlashCmd())
                .addCommands(new Move().getSlashCmd())
                .queue();
    }

    public void reloadCommand(JDA jda, Command c) {
        jda.upsertCommand(c.getSlashCmd()).queue();
    }

    private void setCommands() {
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
        if(!commands.containsKey(commandName)) return null;
        return commands.get(commandName);
    }

    void run(SlashCommandEvent event) {
        final String msg = event.getCommandString();
        if(!event.getGuild().getSelfMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_SEND) &&
           !event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) return;
        final String command = event.getName();
        if (commands.containsKey(command)) {
            if(event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("Nice try, you lowly peasant! Only my masters can command me!")
                        .queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            final List<OptionMapping> options = event.getOptions();
            final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
            commands.get(command).run(args, event);
        }
    }

}
