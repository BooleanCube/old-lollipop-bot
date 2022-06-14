package lollipop.commands.eval;

import groovy.lang.GroovyShell;
import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.AttachmentOption;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

public class Eval implements Command {

    private final GroovyShell engine;
    private final String imports;

    /**
     * Initialize variables and settings in the constructor
     */
    public Eval() {
        this.engine = new GroovyShell();
        this.imports = """
                import java.io.*
                import java.lang.*
                import java.util.*
                import java.nio.*;
                import java.util.concurrent.*
                import net.dv8tion.jda.core.*
                import net.dv8tion.jda.core.entities.*
                import net.dv8tion.jda.core.entities.impl.*
                import net.dv8tion.jda.core.managers.*
                import net.dv8tion.jda.core.managers.impl.*
                import net.dv8tion.jda.core.utils.*
                import lollipop.*;
                import threading.*;
                import discorddb.*;
                """;
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
        return "Evaluates the given expression!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [expression]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.STRING, "expression", "expression to evaluate", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        if(event.getUser().getIdLong() != Constant.OWNER_ID) return;
        if(args.isEmpty()) {
            Tools.wrongUsage(event, this);
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
            if(out == null) {
                event.reply("Executed without error").queue();
                return;
            }
            String response = out.toString();
            if(response.length() <= 2000) event.reply(response).queue();
            else {
                File file = new File(Constant.ABSPATH + "/src/main/java/lollipop/commands/eval/archive/response.txt");
                if(file.exists()) {
                    file.delete();
                    file.createNewFile();
                } else file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write(response);
                fw.flush();
                event.reply("response is too big ->").addFile(file, "message.txt").queue();
            }
        } catch (Exception e) {event.reply(e.getMessage()).queue();}
    }

}
