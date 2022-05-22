package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.List;

public class Anime implements ModelData {

    public int malID;
    public String rating = "Unknown";
    public String title;
    public String art;
    public String url;
    public int episodeCount;
    public double score;
    public String status = "Unkown";
    public String summary;
    public String author;
    public List<String> tags;
    public List<String> episodes;
    public int rank;
    public String trailer;
    public String type = "None";
    public int popularity = 0;

    public String toString() {
        return "Anime [title=" + title + ", art=" + art + ", url=" + url + ", episodeCount=" + episodeCount + ", score=" + score
                + ", status=" + status + ", summary=" + summary + ", author=" + author + ", tags=" + tags + ", episodes=" + episodes + ", popularity=" + popularity + "]";
    }

    @Override
    public void parseData(DataObject data) {
        this.art = data.getObject("images").getObject("jpg").getString("image_url");
        this.malID = data.getInt("mal_id", 0);
        this.status = data.getString("status", "");
        this.rating = data.getString("rating", "");
        this.score = data.getDouble("score", 0);
        this.summary = data.getString("synopsis", "");
        this.title = data.getString("title", "");
        this.url = data.getString("url", "");
        this.rank = data.getInt("rank", Integer.MAX_VALUE);
        this.type = data.getString("type", "");
        this.trailer = data.getObject("trailer").getString("url", "Unkown");
        this.episodeCount = data.getInt("episodes", 0);
    }

    @Override
    public void parseData(DataArray data) {
        // empty
    }

    @Override
    public EmbedBuilder toEmbed() {
        EmbedBuilder e = new EmbedBuilder()
                .setAuthor("ID: " + this.malID, this.url)
                .setDescription(this.summary != null ? this.summary + " [Read More!](" + this.url + ")" : "[Read Here](" + this.url + ")")
                .setTitle(this.title)
                .addField("Type", this.type, true)
                .addField("Rating", this.rating, true)
                .addField("Score", Double.toString(this.score), true)
                .addField("Status", this.status, true)
                .addField("Rank", Integer.toString(this.rank), true)
                .addField("Episode Count", Integer.toString(this.episodeCount), true);
        if(!this.art.equals("")) e.setImage(this.art);
        return e;
    }

}
