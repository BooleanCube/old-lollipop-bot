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
    public String getCategory() { return "Miscellaneous"; }

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
            StringBuilder anime = new StringBuilder();
            manager.getCommands("Anime").forEach(command -> anime.append("**`").append(command.getCommand()).append("`**\n> ").append(command.getHelp().split("\n")[0]).append("\n"));
            StringBuilder fun = new StringBuilder();
            manager.getCommands("Fun").forEach(command -> fun.append("**`").append(command.getCommand()).append("`**\n> ").append(command.getHelp().split("\n")[0]).append("\n"));
            StringBuilder roleplay = new StringBuilder();
            manager.getCommands("Roleplay").forEach(command -> roleplay.append("**`").append(command.getCommand()).append("`**\n> ").append(command.getHelp().split("\n")[0]).append("\n"));
            StringBuilder misc = new StringBuilder();
            manager.getCommands("Miscellaneous").forEach(command -> misc.append("**`").append(command.getCommand()).append("`**\n> ").append(command.getHelp().split("\n")[0]).append("\n"));
            EmbedBuilder e = new EmbedBuilder()
                    .setAuthor(event.getMember().getUser().getName(), event.getMember().getAvatarUrl(), event.getMember().getEffectiveAvatarUrl())
                    .setTitle("lollipop commands");
            e.addField("Anime/Manga", anime.toString(), true);
            e.addField("Fun", fun.toString(), true);
            e.addField("Roleplay", roleplay.toString(), true);
            e.addField("Misc", misc.toString(), true);
            if(event.getMember().getIdLong() == CONSTANT.OWNERID) {
                StringBuilder owner = new StringBuilder();
                manager.getCommands("Owner").forEach(command -> owner.append("**`").append(command.getCommand()).append("`**\n> ").append(command.getHelp().split("\n")[0]).append("\n"));
                e.addField("Owner", owner.toString(), true);
            }
            event.getChannel().sendMessageEmbeds(e.build()).queue();
            return;
        }
        Command command = manager.getCommand(String.join("", args));
        if(command == null) {
            event.getChannel().sendMessage("The command `" + String.join("", args) + "` does not exist!\n" +
                    "Use `" + CONSTANT.PREFIX + command.getCommand() + "` for a list of all my commands!").queue();
            return;
        }
        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("Command Help: `" + command.getCommand() + "`")
                        .setDescription(command.getHelp())
                        .build()
        ).queue();
    }
}
