package lollipop.commands;

import com.beust.ah.A;
import lollipop.Command;
import lollipop.Constant;
import lollipop.Database;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Leaderboard implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"leaderboard"};
    }

    @Override
    public String getCategory() {
        return "Fun";
    }

    @Override
    public String getHelp() {
        return "Show top 10 lollipop users in the guild!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this).addOptions(
                new OptionData(OptionType.STRING, "scope", "guild / global", true)
                        .addChoice("guild", "guild")
                        .addChoice("global", "global")
        );
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

        final java.util.List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());

        if(args.get(0).equals("guild")) {
            ArrayList<String> leaderboard = Database.getLeaderboard(event.getGuild());
            String leaderboardText = String.join("\n\n", leaderboard);
            int userRank = Database.getUserGuildRank(event.getUser().getId(), event.getGuild());
            int userLollipops = Database.getUserBalance(event.getUser().getId());
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(event.getGuild().getName() + "'s leaderboard")
                    .setDescription("> **" + userRank + ".** " + event.getUser().getAsTag() + " (" + userLollipops + " \uD83C\uDF6D)")
                    .addField(" ",leaderboardText, true)
                    .setThumbnail("https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                    .setFooter("Vote for lollipop on top.gg to increase your multiplier!");
            event.replyEmbeds(builder.build()).queue();
        } else if(args.get(0).equals("global")) {
            ArrayList<String> leaderboard = Database.getLeaderboard(event.getJDA());
            String leaderboardText = String.join("\n\n", leaderboard);
            int userRank = Database.getUserGlobalRank(event.getUser().getId());
            int userLollipops = Database.getUserBalance(event.getUser().getId());
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Global leaderboard")
                    .setDescription("> **" + userRank + ".** " + event.getUser().getAsTag() + " (" + userLollipops + " \uD83C\uDF6D)")
                    .addField(" ",leaderboardText, true)
                    .setThumbnail("https://www.dictionary.com/e/wp-content/uploads/2018/11/lollipop-emoji.png")
                    .setFooter("Vote for lollipop on top.gg to increase your multiplier!");
            event.replyEmbeds(builder.build()).queue();
        }
    }

}
