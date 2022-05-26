package lollipop;

import lollipop.commands.RandomAnime;
import lollipop.commands.*;
import lollipop.commands.duel.Duel;
import lollipop.commands.duel.Move;
import lollipop.commands.search.Search;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Manager {

    private final Map<String, Command> commands = new HashMap<>();

    public Manager() {
        setCommands();
    }

    /**
     * Reload all slash commands to all shards
     * @param jda current shard
     */
    public void reloadCommands(JDA jda) {
        //update all commands
        jda.updateCommands().addCommands(
                new Duel().getSlashCmd(),
                new Move().getSlashCmd(),
                new Avatar().getSlashCmd(),
                new Baka().getSlashCmd(),
                new BitesTheDust().getSlashCmd(),
                new BotInfo().getSlashCmd(),
                new Eat().getSlashCmd(),
                new Gif().getSlashCmd(),
                new Headbutt().getSlashCmd(),
                new Help(this).getSlashCmd(),
                new Hentai().getSlashCmd(),
                new Hinokami().getSlashCmd(),
                new InfiniteVoid().getSlashCmd(),
                new Janken().getSlashCmd(),
                new Latest().getSlashCmd(),
                new Onigiri().getSlashCmd(),
                new Ora().getSlashCmd(),
                new Pat().getSlashCmd(),
                new Ping().getSlashCmd(),
                new Punch().getSlashCmd(),
                new RandomAnime().getSlashCmd(),
                new RandomQuote().getSlashCmd(),
                new Rasengan().getSlashCmd(),
                new Search().getSlashCmd(),
                new Top().getSlashCmd()
        ).queue();
    }

    /**
     * Reload all slash commands for a specific guild
     * @param g guild
     */
    public void reloadCommands(Guild g) {
        //update all commands
        CommandData dashCmd = new Dashboard().getSlashCmd();
        CommandData evalCmd = new Eval().getSlashCmd();
        g.updateCommands().addCommands(
                new Duel().getSlashCmd(),
                new Move().getSlashCmd(),
                new Avatar().getSlashCmd(),
                new Baka().getSlashCmd(),
                new BitesTheDust().getSlashCmd(),
                new BotInfo().getSlashCmd(),
                new Eat().getSlashCmd(),
                new Gif().getSlashCmd(),
                new Headbutt().getSlashCmd(),
                new Help(this).getSlashCmd(),
                new Hentai().getSlashCmd(),
                new Hinokami().getSlashCmd(),
                new InfiniteVoid().getSlashCmd(),
                new Janken().getSlashCmd(),
                new Latest().getSlashCmd(),
                new Onigiri().getSlashCmd(),
                new Ora().getSlashCmd(),
                new Pat().getSlashCmd(),
                new Ping().getSlashCmd(),
                new Punch().getSlashCmd(),
                new RandomAnime().getSlashCmd(),
                new RandomQuote().getSlashCmd(),
                new Rasengan().getSlashCmd(),
                new Search().getSlashCmd(),
                new Top().getSlashCmd()
        ).queue();
        g.updateCommands()
                .addCommands(dashCmd.setDefaultEnabled(false), evalCmd.setDefaultEnabled(false))
        .queue(c -> c.forEach(cmd -> g.updateCommandPrivilegesById(cmd.getId(), CommandPrivilege.enableUser(Constant.OWNER_ID)).queue()));
    }

    /**
     * Reload a singular command for all shards
     * @param jda current shard
     * @param c command
     */
    public void reloadCommand(JDA jda, Command c) {
        jda.upsertCommand(c.getSlashCmd()).queue();
    }

    /**
     * Reload a singular command to a singular guild
     * @param g guild
     * @param c command
     */
    public void reloadCommand(Guild g, Command c) {
        g.upsertCommand(c.getSlashCmd()).queue();
    }

    /**
     * Set command manager commands
     */
    private void setCommands() {
        addCommand(new Help(this));
        addCommand(new Gif());
        addCommand(new Ping());
        addCommand(new Search());
        addCommand(new BotInfo());
        addCommand(new Avatar());
        addCommand(new Eval());
        addCommand(new Dashboard());
        addCommand(new Ora());
        addCommand(new Janken());
        addCommand(new Latest());
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
        addCommand(new RandomAnime());
        addCommand(new Top());
        addCommand(new Punch());
        addCommand(new Duel());
        addCommand(new Move());
    }

    /**
     * Adds command to command manager
     * @param c command
     */
    private void addCommand(Command c) {
        if(!commands.containsKey(c.getAliases()[0])) for(String cmd : c.getAliases()) commands.put(cmd, c);
    }

    /**
     * Gets all the commands stored in the command manger and filters out for a specific command category
     * @param category command category
     * @return collection of commands
     */
    public Collection<Command> getCommands(String category) {
        ArrayList<Command> r = new ArrayList<>();
        List<Command> values = commands.values().stream().filter(c -> c.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
        for(Command c : values) if(!r.contains(c)) r.add(c);
        return r;
    }

    /**
     * Gets a list of all commands from the command manager
     * @return list of commands
     */
    public Collection<Command> getCommands() {
        ArrayList<Command> r = new ArrayList<>();
        List<Command> values = new ArrayList<>(commands.values());
        for(Command c : values) if(!r.contains(c)) r.add(c);
        return r;
    }

    /**
     * Get slash command from the command name
     * @param commandName command name
     * @return Command corresponding to the
     */
    public Command getCommand(String commandName) {
        if (commandName == null) return null;
        if(!commands.containsKey(commandName)) return null;
        return commands.get(commandName);
    }

    /**
     * Run the corresponding code to the corresponding command stored in the database
     * @param event slash command interaction event
     */
    void run(SlashCommandInteractionEvent event) {
        final String msg = event.getCommandString();
        if(!event.getGuild().getSelfMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_SEND) &&
           !event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) return;
        final String command = event.getName();
        if (commands.containsKey(command)) {
            if(event.getMember().getUser().isBot()) {
                event.reply("Nice try, you lowly peasant! Only my masters can command me!")
                        .queue(m -> m.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            commands.get(command).run(event);
        }
    }

}
