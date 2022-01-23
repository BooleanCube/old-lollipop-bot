package lollipop.commands;

import groovy.lang.GroovyShell;
import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class Eval implements Command {

    private final GroovyShell engine;
    private final String imports;

    public Eval() {
        this.engine = new GroovyShell();
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.core.*\n" +
                "import net.dv8tion.jda.core.entities.*\n" +
                "import net.dv8tion.jda.core.entities.impl.*\n" +
                "import net.dv8tion.jda.core.managers.*\n" +
                "import net.dv8tion.jda.core.managers.impl.*\n" +
                "import net.dv8tion.jda.core.utils.*\n";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"eval"};
    }

    @Override
    public String getCategory() {
        return "Owner";
    }

    @Override
    public String getHelp() {
        return "Evaluates the given expression!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [expression]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.STRING, "expression", "expression to evaluate", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(event.getUser().getIdLong() != CONSTANT.OWNERID) return;
        if(args.isEmpty()) {
            Tools.wrongUsage(event.getTextChannel(), this);
            return;
        }
        try {
            engine.setProperty("args", args);
            engine.setProperty("event", event);
            engine.setProperty("message", event.getCommandString());
            engine.setProperty("channel", event.getChannel());
            engine.setProperty("jda", event.getJDA());
            engine.setProperty("guild", event.getGuild());
            engine.setProperty("member", event.getMember());
            engine.setProperty("selfmember", event.getGuild().getSelfMember());
            for(Member m : event.getGuild().getMembers()) {
                String nick = m.getNickname();
                if(nick == null) nick = m.getUser().getName();
                engine.setProperty(nick.toLowerCase(), event.getGuild().getMembersByName(m.getUser().getName(), true).get(0));
            }
            String script = imports + event.getCommandString().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);
            event.getChannel().sendMessage(out == null ? "Executed without error" : out.toString()).queue();
        } catch (Exception e) {event.getChannel().sendMessage(e.getMessage()).queue();}
    }

}
