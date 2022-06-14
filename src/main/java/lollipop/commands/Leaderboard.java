package lollipop.commands;

import com.beust.ah.A;
import lollipop.Command;
import lollipop.Constant;
import lollipop.Database;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Leaderboard implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"leaderboard"};
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    @Override
    public String getHelp() {
        return "Show top 10 lollipop users in the guild!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if(!event.getInteraction().isFromGuild()) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.red)
                            .setDescription("This command can only be used in guilds!")
                            .build()
            ).queue();
            return;
        }

        ArrayList<String> leaderboard = Database.getLeaderboard(event.getGuild());
        String leaderboardText = String.join("\n\n", leaderboard);
        int userRank = Database.getUserRank(event.getUser().getId(), event.getGuild());
        int userLollipops = Database.getUserBalance(event.getUser().getId());
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(event.getGuild().getName() + "'s leaderboard")
                .setDescription("> **" + userRank + ".** " + event.getUser().getAsTag() + " (" + userLollipops + " \uD83C\uDF6D)")
                .addField(" ",leaderboardText, true)
                .setThumbnail("https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                .setFooter("Vote for lollipop on top.gg to increase your multiplier!");
        event.replyEmbeds(builder.build()).queue();
    }

}
