package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Hinokami implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"hinokami"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Hinokami Kagura Dance!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [user]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "specified member", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://tenor.com/view/tanjiro-demon-slayer-anime-kimetsu-gif-18996636", "https://tenor.com/view/tanjiro-kamado-hinokami-kagura-dance-hinokami-dance-breathing-rui-spider-gif-14747622", "https://tenor.com/view/tanjiro-fire-gif-21704386", "https://tenor.com/view/demon-slayer-season2-tanjiro-hinokami-kagura-gif-23218698", "https://tenor.com/view/hinokami-kagura-dance-clear-blue-sky-gif-21678253", "https://tenor.com/view/demon-slayer-slash-blade-gif-14756436"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        if(Objects.requireNonNull(event.getMember()).getIdLong() == target.getIdLong()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.reply("**Hinokami Kagura!**\n" + target.getAsMention() + " had their head chopped off by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
