package awatch.model;

import awatch.ModelData;
import lollipop.pages.CharacterList;
import lollipop.pages.EpisodeList;
import lollipop.pages.Newspaper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Anime Model
 */
public class Anime implements ModelData {

    public int malID;
    public String rating = "Unknown";
    public String title;
    public String titleJapanese;
    public String art;
    public String url;
    public int episodeCount;
    public double score;
    public String status = "Unkown";
    public String summary;
    public String author;
    public int rank;
    public int popularity = 0;
    public String season = "Unknown";
    public String trailer = "Unknown";
    public String source = "Unknown";
    public String type = "None";
    public String aired = "Unknown";
    public String duration = "";
    public String broadcast = "";
    public String studios = "None";
    public String genres = "None";
    public String demographics = "None";
    public String producers = "Unknown";
    public String lastAired = null;

    public MessageEmbed stats = null;
    public Newspaper news = null;
    public CharacterList characterList = null;
    public EpisodeList episodeList = null;
    public MessageEmbed themes = null;
    public MessageEmbed review = null;
    public MessageEmbed recommendations = null;

    /**
     * Returns a string with all data
     * @return string
     */
    public String toString() {
        return "Anime [title=" + title + ", art=" + art + ", url=" + url + ", episodeCount=" + episodeCount + ", score=" + score
                + ", status=" + status + ", summary=" + summary + ", author=" + author + ", popularity=" + popularity + "]";
    }

    /**
     * Parses all the data and defines the Anime Object
     * @param data data object from json
     */
    @Override
    public void parseData(DataObject data) {
        this.art = data.getObject("images").getObject("jpg").getString("image_url", "");
        this.malID = data.getInt("mal_id", 0);
        this.status = data.getString("status", "");
        this.rating = data.getString("rating", "");
        this.score = data.getDouble("score", 0);
        this.summary = data.getString("synopsis", "");
        this.title = data.getString("title", "");
        this.titleJapanese = data.getString("title_japanese", "わからない");
        this.url = data.getString("url", "");
        this.rank = data.getInt("rank", Integer.MAX_VALUE);
        if(this.rank == 0) rank = Integer.MAX_VALUE;
        this.type = data.getString("type", "");
        this.trailer = data.getObject("trailer").getString("url", "Unkown");
        this.episodeCount = data.getInt("episodes", 0);
        this.source = data.getString("source", "Unknown");
        this.season = data.getString("season", "") + ", " + data.getInt("year", 0);
        if(this.season.equals(", 0")) this.season = "Unknown";
        else this.season = (char)(this.season.charAt(0) & 0x5f) + this.season.substring(1);
        DataObject airedData = data.getObject("aired");
        this.aired = airedData.getString("string", "Unknown");
        this.lastAired = airedData.getString("to", "null");
        if(this.lastAired.equals("null")) this.lastAired = airedData.getString("from", "02496");
        this.popularity = data.getInt("popularity", Integer.MAX_VALUE);
        if(this.popularity == 0) this.popularity = Integer.MAX_VALUE;
        this.duration = data.getString("duration", "Unknown");
        this.broadcast = data.getObject("broadcast").getString("string", "Unknown");
        StringBuilder sb = new StringBuilder();
        DataArray studios = data.getArray("studios");
        for(int i=0; i<studios.length(); i++) {
            DataObject studio = studios.getObject(i);
            sb.append("[").append(studio.getString("name", "Unknown")).append("](").append(studio.getString("url","")).append("), ");
        }
        this.studios = sb.toString().trim();
        if(this.studios.equals("")) this.studios = "Unknown";
        else this.studios = this.studios.substring(0, this.studios.length()-1);
        sb = new StringBuilder();
        DataArray genres = data.getArray("genres");
        for(int i=0; i<genres.length(); i++) {
            DataObject genre = genres.getObject(i);
            sb.append("[").append(genre.getString("name", "Unknown")).append("](").append(genre.getString("url","")).append("), ");
        }
        this.genres = sb.toString().trim();
        if(this.genres.equals("")) this.genres = "Unknown";
        else this.genres = this.genres.substring(0, this.genres.length()-1);
        sb = new StringBuilder();
        DataArray demographics = data.getArray("demographics");
        for(int i=0; i<demographics.length(); i++) {
            DataObject demographic = demographics.getObject(i);
            sb.append("[").append(demographic.getString("name", "Unknown")).append("](").append(demographic.getString("url","")).append("), ");
        }
        this.demographics = sb.toString().trim();
        if(this.demographics.equals("")) this.demographics = "None";
        else this.demographics = this.demographics.substring(0, this.demographics.length()-1);
        sb = new StringBuilder();
        DataArray producers = data.getArray("producers");
        for(int i=0; i<Math.min(12,producers.length()); i++) {
            DataObject producer = producers.getObject(i);
            sb.append("[").append(producer.getString("name", "Unknown")).append("](").append(producer.getString("url","")).append("), ");
        }
        this.producers = sb.toString().trim();
        if(this.producers.equals("")) this.producers = "Unknown";
        else this.producers = this.producers.substring(0, this.producers.length()-1);
    }

    /**
     * Parses all the data and defines the Anime Object
     * @param data data array
     */
    @Override
    public void parseData(DataArray data) {
        // empty
    }

    /**
     * Compresses all of the data into an EmbedBuilder
     * @return EmbedBuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        EmbedBuilder e = new EmbedBuilder()
                .setAuthor("ID: " + this.malID, this.url)
                .setDescription(this.summary != null ? this.summary + " [Read More!](" + this.url + ")" : "[Read Here](" + this.url + ")")
                .setTitle(this.title + " (" + this.titleJapanese + ")")
                .addField("Type", this.type, true)
                .addField("Rating", this.rating, true)
                .addField("Source", this.source, true)
                .addField("Score", this.score + "/10", true)
                .addField("Rank", "#"+this.rank, true)
                .addField("Popularity", "#"+this.popularity, true)
                .addField("Status", this.status, true)
                .addField("Aired", this.aired, true)
                .addField("Season", this.season, true)
                .addField("Episode Count", this.episodeCount + " episode(s)", true)
                .addField("Broadcast", this.broadcast, true)
                .addField("Duration", this.duration, true)
                .addField("Demographics", this.demographics, true)
                .addField("Genres", this.genres, true)
                .addField("Studios", this.studios, true)
                .addField("Producers", this.producers, false);
        if(!this.art.equals("")) e.setImage(this.art);
        return e;
    }

}
