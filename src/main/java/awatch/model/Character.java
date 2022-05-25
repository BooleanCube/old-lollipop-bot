package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Character Model
 */
public class Character implements ModelData {

    public String art;
    public String name;
    public int malID;
    public String alternativeNames = "None";
    public String url;
    public String anime = "None";
    public String manga = "None";

    /**
     * Returns a string with all the data
     * @return string
     */
    public String toString() {
        return "Charcter [" + malID + ": " + name + "]";
    }

    /**
     * Parses all the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        this.art = data.getString("image_url");
        this.name = data.getString("name");
        if(data.getArray("manga").length() > 0)
            this.manga = "[" + data.getArray("manga").getObject(0).get("name") + "](" + data.getArray("manga").getObject(0).get("url") + ")";
        this.malID = data.getInt("mal_id");
        if(data.getArray("alternative_names").length() > 0)
            this.alternativeNames = data.getArray("alternative_names")
                    .toList()
                    .stream()
                    .map(a -> Objects.toString(a, null))
                    .collect(Collectors.joining(", "));
        this.url = data.getString("url");
        if(data.getArray("anime").length() > 0)
            this.anime = "[" + data.getArray("anime").getObject(0).get("name") + "](" + data.getArray("anime").getObject(0).get("url") + ")";
    }

    /**
     * Parses all the data
     * @param data
     */
    @Override
    public void parseData(DataArray data) {
        // empty
    }

    /**
     * Compresses all the data into an EmbedBuilder
     * @return embedbuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setAuthor("ID: " + this.malID, this.url)
                .setTitle(this.name)
                .addField("Alternative Names", this.alternativeNames, false)
                .addField("Latest Anime", this.anime, false)
                .addField("Latest Manga", this.manga, false)
                .setImage(this.art);
    }

}
