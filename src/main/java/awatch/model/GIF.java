package awatch.model;

import awatch.ModelData;
import lollipop.Cache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * GIF Model
 */
public class GIF implements ModelData {

    public String url;

    public GIF() {}

    /**
     * Returns a string with all the data
     * @return string
     */
    public String toString() {
        return Arrays.toString(new String[]{url});
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        this.url = data.getString("response");
        try {
            Cache.addGifToCache(this.url);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataArray data) {
        //empty
    }

    /**
     * Compresses all of the data into an EmbedBuilder
     * @return embedbuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setImage(url);
    }

}
