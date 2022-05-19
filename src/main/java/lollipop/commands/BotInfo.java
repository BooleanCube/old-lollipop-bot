package lollipop.commands;

import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class BotInfo implements Command {
    @Override
    public String[] getAliases() { return new String[] {"botinfo"}; }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    @Override
    public String getHelp() {
        return "Displays information about the bot!\nUsage: `" + Constant.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Bot Information")
                .setDescription("Lollipop is an anime/manga discord bot which allows any user to search for an anime or a manga from the web and get the results on discord and has many features like fun roleplay commands, useful utility commands and other fun commands.\n" +
                        "> [Bot Invite Link](https://discord.com/api/oauth2/authorize?client_id=919061572649910292&permissions=1515854359872&scope=bot%20applications.commands)\n" +
                        "> [Github Repository](https://github.com/BooleanCube/lollipop-bot)\n" +
                        "> [Discord Bot List](https://discordbotlist.com/bots/lollipop-4786)\n" +
                        "> [Top.gg](https://top.gg/bot/919061572649910292)\n")
                .addField("Support", "Did you encounter a bug? Join this [server](https://discord.gg/3ZDpPyR) and report in a support channel.", false)
                .addField("Developer", "**BooleanCube** (" + event.getJDA().getShardManager().getGuildById(740316079523627128l).getOwner().getUser().getAsTag() + ")\n[MyAnimeList](https://myanimelist.net/profile/BooleanCube) - [Playlist](https://open.spotify.com/playlist/4KnWT1hszQuBi4IaKdm8Pk?si=91e0fe7e73b54853) - [Discord](https://discord.gg/3ZDpPyR) - [Github](https://github.com/BooleanCube) - [Youtube](https://www.youtube.com/channel/UCsivrachJyFVLi7V60lrd6g)", false)
                .setFooter("konnichiwa, watashi wa lollipop desu")
                .setThumbnail(event.getGuild().getSelfMember().getEffectiveAvatarUrl())
                .build()
        ).queue();
    }
}
