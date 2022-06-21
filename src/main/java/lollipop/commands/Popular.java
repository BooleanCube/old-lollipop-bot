package lollipop.commands;

import lollipop.API;
import lollipop.Command;
import lollipop.Constant;
import lollipop.Tools;
import lollipop.pages.AnimePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Popular implements Command {

    public static HashMap<Long, AnimePage> messageToPage = new HashMap<>();

    @Override
    public String[] getAliases() {
        return new String[]{"popular"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Gets the 25 most popular anime/manga in the world!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [type]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOptions(
                        new OptionData(OptionType.STRING, "type", "anime / manga", true)
                                .addChoice("anime", "anime")
                                .addChoice("manga", "manga")
                );
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        API api = new API();
        if(args.get(0).equals("anime")) {
            InteractionHook msg = event.replyEmbeds(
                    new EmbedBuilder()
                            .setDescription("Getting the `25 Most Popular` anime...")
                            .build()
            ).complete();
            Message message = msg.retrieveOriginal().complete();
            ScheduledFuture<?> timeout = msg.editOriginalEmbeds(new EmbedBuilder()
                    .setColor(Color.red)
                    .setDescription("Could not get the 25 most popular animes! Please try again later!")
                    .build()
            ).queueAfter(5, TimeUnit.SECONDS, me -> messageToPage.remove(message.getIdLong()));
            messageToPage.put(message.getIdLong(), new AnimePage(null, message, 1, event.getUser(), timeout));
            api.getPopularAnime(msg);
        } else if(args.get(0).equals("manga")) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setDescription("This feature has not been implemented yet, please wait patiently!")
                            .setColor(Color.red)
                            .build()
            ).queue();
        }
    }

}
