package lollipop.commands;

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

public class Rasengan implements Command {
    @Override
    public String[] getAliases() {
        return new String[] {"rasengan"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Rasengan!";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", true);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
        String[] gifs = {"https://tenor.com/view/naruto-rasengan-anime-running-gif-16159006", "https://tenor.com/view/naruto-anime-rasengan-gif-11805675", "https://tenor.com/view/naruto-rasengan-clone-anime-gif-7239436", "https://tenor.com/view/naruto-rasengan-gif-8212746", "https://tenor.com/view/naruto-rasengan-clones-naruto-shippuden-power-gif-15111421", "https://tenor.com/view/rasengan-minato-obito-uchiha-anime-gif-12425387"};
        Member target = Tools.getEffectiveMember(event.getGuild(), String.join(" ", args));
        if(target == null) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Could not find the specified member!").setColor(Color.red).build()).queue();
            return;
        }
        if(Objects.requireNonNull(event.getMember()).getIdLong() == target.getIdLong()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.reply("**RASENGAN!**\n" + target.getAsMention() + " was blasted away by " + event.getMember().getAsMention()).queue();
        event.getChannel().sendMessage(gifs[(int)(Math.random()*gifs.length)]).queue();
    }
}
