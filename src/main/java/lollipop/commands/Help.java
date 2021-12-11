package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Manager;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Help implements Command {
    Manager manager;
    public Help(Manager m) {
        this.manager = m;
    }
    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows you a list of all the commands!\n" +
                "Usage: `" + CONSTANT.PREFIX + getCommand() + " <command(optional)>`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.size() > 1) {
            Tools.wrongUsage(event.getTextChannel(), this);
            return;
        }
        if(args.isEmpty()) {
            EmbedBuilder e = new EmbedBuilder()
                    .setTitle("A list of all my commands:");
            manager.getCommands().forEach(command -> {
                e.appendDescription("`").appendDescription(command.getCommand()).appendDescription("`\n");
            });
            event.getChannel().sendMessageEmbeds(e.build()).queue();
            return;
        }
        Command command = manager.getCommand(String.join("", args));
        if(command == null) {
            event.getChannel().sendMessage("The command `" + String.join("", args) + "` does not exist!\n" +
                    "Use `" + CONSTANT.PREFIX + command.getCommand() + "` for a list of all my commands!").queue();
            return;
        }
        event.getChannel().sendMessage("Command help for `" + command.getCommand() + "`\n" +
                command.getHelp()).queue();
    }
}