package lollipop.commands;

import lollipop.Command;
import lollipop.CommandType;
import lollipop.Constant;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Policy implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"policy"};
    }

    @Override
    public CommandType getCategory() {
        return CommandType.MISCELLANEOUS;
    }

    @Override
    public String getHelp() {
        return "Displays the privacy policy lollipop follows.\n" +
                "Usage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyEmbeds(
                new EmbedBuilder()
                        .setTitle("Privacy Policy")
                        .setDescription("Lollipop, as a discord bot, abides by the following privacy policy:")
                        .addField("What data do you collect?", "The only data stored and collected is individual lollipop points with correspondence to the user's ID and anonymous command usage data. I can assure the users that none of the data collected is private and publicly accessible by everybody on the platform itself.", false)
                        .addField("Why do you need the data?", "To improve quality of the discord bot and add more new and fun features likeable by most of the users.", false)
                        .addField("How do you use the data?", "All data collected is only for observation and implementation purposes. None of the data will be leaked, exposed or sold and will be collected anonymously.", false)
                        .addField("Who do you share your collected data with?", "None of the data collected will be shared with anybody except Discord, Discord Bot Lists and the users themselves.", false)
                        .addField("How can users contact you if they have concerns about the bot?", "Lollipop has a support server which can easily be found by using the command `/support` where I will actively solve any problems related to the bot", false)
                        .addField("If you store data, how can users have that data removed?", "Stored data can't be removed by the user themselves directly, however, upon request I can remove the data from the database excluding anonymous data collected.", false)
                        .setFooter("Signed by BooleanCube")
                        .build()
        ).queue();
    }
}
