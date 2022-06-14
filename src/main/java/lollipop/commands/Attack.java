package lollipop.commands;

import lollipop.Command;
import lollipop.Constant;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;

public class Attack implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"attack"};
    }

    @Override
    public String getCategory() {
        return "Roleplay";
    }

    @Override
    public String getHelp() {
        return "Attack somebody!\bUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "mention a user", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        String[] gifs = new String[] {
                "https://c.tenor.com/1xHAx5bI1DQAAAAC/demon-slayer-kimetsu-no-yaiba.gif", "https://c.tenor.com/SSgZ9SP_NeAAAAAC/tanjiro-kamado-kimetsu-no-yaiba.gif", "https://c.tenor.com/bRvQowf8RfkAAAAC/headbutt-fight.gif", "https://c.tenor.com/XhsmfZrj8EMAAAAC/dbz-gif.gif", "https://c.tenor.com/0zAC3rdS_5kAAAAC/anime-headbutt.gif",
                "https://c.tenor.com/AlygbzZXIn8AAAAC/tanjiro-demon.gif", "https://c.tenor.com/XySV6ySMPF4AAAAS/tanjiro-kamado-hinokami-kagura-dance.gif", "https://c.tenor.com/JiIpyKUl08wAAAAd/tanjiro-fire.gif", "https://c.tenor.com/dDE8ASg_ltMAAAAC/demon-slayer-season2.gif", "https://c.tenor.com/E3pWFs_Xt7AAAAAS/hinokami-kagura-dance.gif", "https://c.tenor.com/koVv87guk6QAAAAd/demon-slayer-slash.gif",
                "https://c.tenor.com/84Y17eI-b0oAAAAS/infinite-void-gojo.gif", "https://c.tenor.com/MfMS51gTUc4AAAAS/satoru-gojo-domain-expansion.gif", "https://c.tenor.com/LOrTA4poJjEAAAAS/gojo-blindfold.gif", "https://c.tenor.com/CrX9wY8ibikAAAAS/sawunn.gif", "https://c.tenor.com/Tt0MU3RgnoQAAAAd/jjk-gojo.gif", "https://c.tenor.com/QM3oFaOSQIgAAAAS/jujustu-kaisen-satoru-gojo.gif", "https://c.tenor.com/AydW1nG8SpoAAAAC/anime.gif",
                "https://tenor.com/view/jojo-jojos-bizarre-adventures-ora-ora-ora-the-world-stand-gif-15566871", "https://tenor.com/view/jojo-jotaro-dio-brando-kujo-gif-21583125", "https://tenor.com/view/dio-jojo-jjba-anime-fire-gif-16677683",
                "https://c.tenor.com/wFb6MPreo9sAAAAS/roronoa-zoro-purgatory-onigiri.gif", "https://c.tenor.com/xwjN54qq-PAAAAAC/onepiece.gif", "https://c.tenor.com/RHqtJ5bvKtYAAAAC/one-piece.gif",
                "https://c.tenor.com/xZXvHLQWTP8AAAAC/jojo-star-platinum.gif", "https://c.tenor.com/LytxJSf81m4AAAAC/ora-beatdown-oraoraora.gif", "https://c.tenor.com/XQWVeTsLxWMAAAAC/jojos-bizarre-adventure-ora-ora.gif", "https://c.tenor.com/kXeAcAl6PmQAAAAS/star-platinum.gif", "https://c.tenor.com/Hf9MuA4oUrQAAAAS/star-platinum-ora.gif", "https://c.tenor.com/HzfPT3oEMMEAAAAC/star-platinum-the-world-jojo.gif",
                "https://c.tenor.com/VXJeIRgdC_oAAAAC/naruto-rasengan.gif", "https://c.tenor.com/owUrJK4G2CoAAAAC/naruto-anime.gif", "https://c.tenor.com/LZlkPMGI9mMAAAAC/naruto-rasengan.gif", "https://c.tenor.com/sWqQhy0AExoAAAAC/naruto-rasengan.gif", "https://c.tenor.com/4FFD2H294mAAAAAC/naruto-rasengan.gif", "https://c.tenor.com/YdgIUNRqy3wAAAAC/rasengan-minato.gif",
        };
        User target = options.get(0).getAsUser();
        if(event.getUser().getIdLong() == target.getIdLong()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("You can't use Roleplay Commands on yourself!").setColor(Color.red).build()).queue();
            return;
        }
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("⚔ **ATTACK!!!** ⚔ \n" + target.getAsMention() + " was attacked by " + event.getUser().getAsMention())
                .setImage(gifs[(int)(Math.random()*gifs.length)])
                .build()).queue();
    }

}
