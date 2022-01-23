package lollipop.commands;

import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
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
        return "Displays information about the bot!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + "`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this);
    }

    @Override
    public void run(List<String> args, SlashCommandEvent event) {
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Bot Information")
                .setDescription("lollipop is an anime/manga discord bot which provides with fun commands!\n" +
                        "> [Bot Invite Link](https://discord.com/api/oauth2/authorize?client_id=919061572649910292&permissions=1515854359872&scope=bot%20applications.commands)\n" +
                        "> [Github Repository](https://github.com/BooleanCube/lollipop-bot)\n")
                .addField("Support", "Did you encounter a bug? Join this [server](https://discord.gg/3ZDpPyR) and report in a support channel.", false)
                .addField("Author", "**BooleanCube** (" + event.getJDA().getShardManager().getGuildById(740316079523627128l).getOwner().getUser().getAsTag() + ")\n[MyAnimeList](https://myanimelist.net/profile/BooleanCube) - [Playlist](https://open.spotify.com/playlist/4KnWT1hszQuBi4IaKdm8Pk?si=91e0fe7e73b54853) - [Discord](https://discord.gg/3ZDpPyR) - [Github](https://github.com/BooleanCube) - [Youtube](https://www.youtube.com/channel/UCsivrachJyFVLi7V60lrd6g)", false)
                .setFooter("konnichiwa, watashi wa lollipop desu")
                .setAuthor("lollipop", "https://discord.gg/3ZDpPyR", "https://camo.githubusercontent.com/173ac0fe37e2c35233c6270aebdc23bf48e7e151f6cf685f0e71d18f01be1808/68747470733a2f2f692e696d6775722e636f6d2f4346366f674e4c2e6a706567")
                .setThumbnail("https://media.discordapp.net/attachments/919069979377287188/921538899141091438/lloli.jpg?width=230&height=230")
                .build()
        ).queue();
    }
}
