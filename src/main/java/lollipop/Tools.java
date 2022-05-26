package lollipop;

import awatch.controller.AConstants;
import awatch.model.*;
import awatch.model.Character;
import mread.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.awt.*;
import java.util.ArrayList;

public class Tools {

    /**
     * A template for default slash commands in java
     * @param c command
     * @return command data
     */
    public static SlashCommandData defaultSlashCmd(Command c) {
        return Commands.slash(c.getAliases()[0], c.getHelp().split("\n")[0]);
    }

    /**
     * Gets the user given a large range of input types
     * @param g guild
     * @param s user
     * @return member of the server
     */
    public static Member getEffectiveMember(Guild g, String s) {
        Member m = g.getMemberById(s.replaceAll("[<@!>]", ""));
        if (m == null && User.USER_TAG.matcher(s).matches()) m = g.getMemberByTag(s);
        if (m == null) m = g.getMembersByEffectiveName(s, true).get(0);
        if (m == null) m = g.getMembersByName(s, true).get(0);
        return m;
    }

    /**
     * Used when somebody messes up their oppotrunity to get extra credit
     * @param event slash command interaction event
     * @param c command
     */
    public static void wrongUsage(SlashCommandInteractionEvent event, Command c) {
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Wrong Command Usage!")
                .setDescription(c.getHelp())
                .setColor(Color.red)
                .build()
        ).queue();
    }

}
