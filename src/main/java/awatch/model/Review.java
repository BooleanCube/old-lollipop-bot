package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Arrays;

/**
 * Review Model
 */
public class Review implements ModelData {

    public String authorName;
    public String authorIcon;
    public String authorUrl;
    public String url;
    public String details;
    public int votes = 0;
    public int score = 0;

    public Review() {}

    /**
     * Returns a string with all of the data
     * @return string
     */
    public String toString() {
        return Arrays.toString(new String[]{authorName, authorIcon, authorUrl, url, details, Integer.toString(votes), Integer.toString(score)});
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return; }
        DataObject res = arr.getObject(0);
        this.authorName = res.getObject("user").getString("username", "Unkown Name");
        this.authorIcon = res.getObject("user").getObject("images").getObject("jpg").getString("image_url", "https://api-private.atlassian.com/users/63729d1b358a0c5f1c38cf368ad9d693/avatar");
        this.authorUrl = res.getObject("user").getString("url", "https://myanimelist.net/reviews.php");
        this.url = res.getString("url", "https://myanimelist.net/reviews.php");
        this.details = res.getString("review", "Empty Review Description");
        this.votes = res.getInt("votes", 0);
        this.score = res.getObject("scores").getInt("overall", 0);
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataArray data) {
        // empty
    }

    /**
     * Compresses all of the data into an EmbedBuilder
     * @return embedbuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setAuthor(authorName, authorUrl, authorIcon)
                .setTitle("Top Review")
                .setDescription(
                        details.length()>=1970 ?
                                details.substring(0,1960) + "...\n[Read More!](" + url + ")" :
                                details + "\n[Read More!](" + url + ")" )
                .addField("Votes", Integer.toString(votes), true)
                .addField("Score", Integer.toString(score), true);
    }

}
