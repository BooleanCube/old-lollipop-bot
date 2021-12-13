package lollipop;

import awatch.Anime;
import com.bool.readm.model.Manga;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Tools {

    public static void wrongUsage(TextChannel tc, Command c) {
        tc.sendMessage("Wrong Command Usage!\n" + c.getHelp()).queue();
    }

    public static EmbedBuilder mangaToEmbed(Manga m) {
        if(m.summary.length() > 2000) m.summary = m.summary.substring(0, 1000) + "... (Click the title to read more)";
        EmbedBuilder msg = new EmbedBuilder()
                .setAuthor(m.title, MangaAPI.apiPath+m.url)
                .setDescription(m.summary.replaceAll("SUMMARY", "").trim())
                .setImage(MangaAPI.apiPath+m.art)
                .addField("Authors",m.author,true)
                .addField("Chapters",m.chapter,true)
                .addField("Rating", m.rating, true)
                .addField("Status",m.status,true)
                .addField("Tags", String.join(", ", m.tags), false);

        return msg;
    }

    public static EmbedBuilder animeToEmbed(Anime a) {
        if(a==null) {
            EmbedBuilder msg = new EmbedBuilder()
                    .setDescription("Could not find an anime with that search query! Please try again with a valid anime!");
            return msg;
        }
        EmbedBuilder msg = new EmbedBuilder()
                .setAuthor("ID: " + a.malID, a.url)
                .setDescription(a.summary + " (Click on the ID to read more!)")
                .setTitle(a.title)
                .addField("Rating", a.rating, true)
                .addField("Score", Integer.toString(a.score), true)
                .addField("Status", a.status, true)
                .addField("Epsiode Count", Integer.toString(a.episodeCount), true)
                .setImage(a.art);
        return msg;
    }

}
