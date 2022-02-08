package lollipop.commands;

import lollipop.API;
import lollipop.Constant;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchCharacter implements Command {

    @Override
    public String[] getAliases() {
        return new String[] {"searchcharacter", "searchc", "sc"};
    }

    @Override
    public String getCategory() {
        return "Anime";
    }

    @Override
    public String getHelp() {
        return "Searches for a character with the given search query!\nUsage: `" + Constant.PREFIX + getAliases()[0] + " [query]`";
    }

    @Override
    public CommandData getSlashCmd() {
        return Tools.defaultSlashCmd(this)
                .addOption(OptionType.STRING, "query", "search character name", true);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        final List<OptionMapping> options = event.getOptions();
        final List<String> args = options.stream().map(OptionMapping::getAsString).collect(Collectors.toList());
        if(args.isEmpty()) { Tools.wrongUsage(event, this); return; }
        API api = new API();
        String query = String.join(" ", args);
        Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Searching for `" + query + "`...").build()).complete();
        try {
            msg.editMessageEmbeds(Tools.characterToEmbed(api.searchForCharacter(query)).build()).queue();
        }
        catch (IOException e) {
            msg.editMessageEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("Could not find an anime with that search query! Please try again with a valid anime!").build()).queue();
        }
    }

}
