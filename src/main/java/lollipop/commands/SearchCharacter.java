package lollipop.commands;

import lollipop.API;
import lollipop.CONSTANT;
import lollipop.Command;
import lollipop.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;

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
        return "Searches for a character with the given search query!\nUsage: `" + CONSTANT.PREFIX + getAliases()[0] + " [query]`";
    }

    @Override
    public void run(List<String> args, MessageReceivedEvent event) {
        if(args.isEmpty()) { Tools.wrongUsage(event.getTextChannel(), this); return; }
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
