package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Question Model Class for Trivia Games
 */
public class Question implements ModelData {

    public Anime correct;
    public ArrayList<String> options;

    /**
     * Constructor to initialize variables
     * @param c correct anime
     * @param o options to guess from
     */
    public Question(Anime c, ArrayList<String> o) {
        this.correct = c;
        this.options = o;
    }

    /**
     * Generates num amount of options from available
     * @param available available options
     * @param num number of options to generate
     */
    public void generateOptions(HashSet<String> available, int num) {
        for(int i=0; i<num; i++) {
            String title = available.iterator().next();
            this.options.add(title);
            available.remove(title);
        }
    }

    @Override
    public String toString() {
        return correct.toString() + " " + options.toString();
    }

    @Override
    public void parseData(DataObject data) {
        // empty
    }

    @Override
    public void parseData(DataArray data) {
        // empty
    }

    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setTitle("Can you guess this anime?")
                .setDescription(correct.summary != null ? correct.summary : "[ No Summary Found ]")
                .addField("Type", correct.type, true)
                .addField("Rating", correct.rating, true)
                .addField("Score", Double.toString(correct.score), true)
                .addField("Rank", "#"+correct.rank, true)
                .addField("Popularity", "#"+correct.popularity, true)
                .addField("Source", correct.source, true)
                .addField("Status", correct.status, true)
                .addField("Aired", correct.aired, true)
                .addField("Season", correct.season, true)
                .addField("Episode Count", "#"+correct.episodeCount, true)
                .addField("Broadcast", correct.broadcast, true)
                .addField("Duration", correct.duration, true)
                .addField("Studios", correct.studios, true)
                .addField("Genres", correct.genres, true)
                .addField("Demographics", correct.demographics, true)
                .setFooter("Quick! You have 15 seconds to answer!");
    }

}
