package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BitesTheDust implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"biteszadust", "btd"};
    }

    @Override
    public String getCategory() {
        return "Fun";
    }

    @Override
    public String getHelp() {
        return "Deletes your recent messages in a channel!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if (!args.isEmpty()) {
            Tools.wrongUsage(event.getTextChannel(), this);
            return;
        }
        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("This command can't be used because I don't have the `MESSAGE_MANAGE` permission in this server!")
                            .build()
            ).queue();
            return;
        }
        List<Message> msgList = event.getChannel().getHistory().retrievePast(31).complete().stream().filter(m -> m.getMember().getIdLong() == event.getMember().getIdLong()).collect(Collectors.toList());
        event.deferReply().queue();
        try {
            if(msgList.isEmpty()) throw new Exception();
            event.getChannel().purgeMessages(msgList);
            event.getChannel().sendMessage("Successfully travelled back 30 messages in time without leaving any traces behind!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
        } catch(Exception e) {
            event.getChannel().sendMessage("You haven't done anything recently to travel back in time!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
        }
    }
}
