package lollipop.commands;

import lollipop.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Profile implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"profile"};
    }

    @Override
    public String getCategory() {
        return "Fun";
    }

    @Override
    public String getHelp() {
        return "Shows a user's profile!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [user*]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.USER, "user", "Mention user to show their profile.", false);
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

        EmbedBuilder builder = new EmbedBuilder();
        DateTimeFormatter dateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
        Member target;

        if(event.getOptions().size() == 1) {
            target = event.getOptions().get(0).getAsMember();
            if(target == null) {
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.red)
                                .setDescription("Could not find that user! Please try again with a valid member in the options!")
                                .build()
                ).setEphemeral(true).queue();
                return;
            }
            if(target.getUser().isBot()) {
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.red)
                                .setDescription("You can't look at the profile of a bot account!")
                                .build()
                ).setEphemeral(true).queue();
                return;
            }
        }
        else target = event.getMember();

        int lollipops = Database.getUserBalance(target.getId());
        int[] ranks = Database.getUserRank(target.getId(), event.getGuild());
        int guildSize = event.getGuild().getMemberCount();
        int globalSize = Database.getCurrencyUserCount();
        double level = lollipopsToLevel(lollipops);
        String title = "Noob";
        if(level > 80) title = "OwO";
        else if(level > 60) title = "Senpai";
        else if(level > 40) title = "Otaku";
        else if(level > 20) title = "Weeb";
        if(target.isBoosting()) title += ", Booster";
        if(target.isOwner()) title += ", Owner";
        builder.setAuthor(target.getEffectiveName() + "'s profile", "https://top.gg/bot/919061572649910292");
        builder.setThumbnail(target.getEffectiveAvatarUrl());
        builder.setColor(target.getColor());
        String banner = target.getUser().retrieveProfile().complete().getBannerUrl();
        if(banner != null) {
            banner += "?size=512";
            builder.setImage(banner);
        }
        else builder.setImage("https://user-images.githubusercontent.com/47650058/147891305-58aa09b6-2053-4180-9a9a-8c09826567f1.png");
        builder.setDescription("**Title:** `" + title + "`\n");
        builder.addField("Level", level + " \uD83E\uDE99", true);
        builder.addField("Rank", "**Guild:** " + ranks[0] + " / " + guildSize + "\n**Global:** " + ranks[1] + " / " + globalSize, true);
        builder.addField("Lollipops", lollipops + " \uD83C\uDF6D", true);
        builder.addField("Misc",
                target.getEffectiveName() + " | " + target.getAsMention() + " | " + target.getUser().getAsTag() + "\n" +
                        "**Account Creation:** " + target.getUser().getTimeCreated().format(dateFormat) + "\n" +
                        "**Member Join:** " + target.getTimeJoined().format(dateFormat), false);
        builder.setFooter("Vote for lollipop on top.gg to increase your multiplier to " + Constant.MULTIPLIER + "x");
        Runnable success = () -> {
            builder.appendDescription("**Multiplier:** `" + Constant.MULTIPLIER + "x`");
            event.replyEmbeds(builder.build()).queue();
        };
        Runnable failure = () -> {
            builder.appendDescription("**Multiplier:** `1.0x`");
            event.replyEmbeds(builder.build()).queue();
        };
        BotStatistics.sendMultiplier(target.getId(), success, failure);
    }

    /**
     * Lollipops to Level relationship
     * @param lollipops number of lollipops
     * @return current level based on lollipops in double
     */
    private double lollipopsToLevel(int lollipops) {
        return Math.round( ( (Math.sqrt(lollipops+10) - 3.162d) / 4d ) *100 ) / 100d;
    }

}
