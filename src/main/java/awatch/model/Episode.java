package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Arrays;

/**
 * Episode Model
 */
public class Episode implements ModelData {

    public String url;
    public String title;
    public Episode(String u, String t) {
        url = u;
        title = t;
    }
    public Episode() {}

    /**
     * Returns a string with all the data
     * @return string
     */
    public String toString() {
        return Arrays.toString(new String[]{url, title});
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        this.url = data.getString("url", "");
        this.title = data.getString("title_romanji", data.getString("title", ""));
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
        //empty
        return null;
    }

}
