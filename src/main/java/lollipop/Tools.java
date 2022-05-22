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

    public static SlashCommandData defaultSlashCmd(Command c) {
        return Commands.slash(c.getAliases()[0], c.getHelp().split("\n")[0]);
    }

    public static Member getEffectiveMember(Guild g, String s) {
        Member m = g.getMemberById(s.replaceAll("[<@!>]", ""));
        if (m == null && User.USER_TAG.matcher(s).matches()) m = g.getMemberByTag(s);
        if (m == null) m = g.getMembersByEffectiveName(s, true).get(0);
        if (m == null) m = g.getMembersByName(s, true).get(0);
        return m;
    }

    public static void wrongUsage(SlashCommandInteractionEvent event, Command c) {
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Wrong Command Usage!")
                .setDescription(c.getHelp())
                .setColor(Color.red)
                .build()
        ).queue();
    }

    public static EmbedBuilder mangaToEmbed(Manga m) {
        if(m.summary.length() > 2000) m.summary = m.summary.substring(0, 1000) + "... [Read More!](" + m.url + ")";
        return new EmbedBuilder()
                .setAuthor(m.title, AConstants.readmAPI+m.url)
                .setDescription(m.summary.replaceAll("SUMMARY", "").trim())
                .setImage(AConstants.readmAPI +m.art)
                .addField("Authors",m.author,true)
                .addField("Chapters",m.chapter,true)
                .addField("Rating", m.rating, true)
                .addField("Status", m.status,true)
                .addField("Tags", String.join(", ", m.tags), false);
    }

}
