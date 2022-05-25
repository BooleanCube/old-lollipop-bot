package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Arrays;

public class Quote implements ModelData {

    public String quote;
    public String character;
    public String anime;

    public Quote() {}

    /**
     * Returns a string with all the data
     * @return string
     */
    public String toString() {
        return Arrays.toString(new String[]{quote, character, anime});
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        this.anime = data.getString("anime");
        this.character = data.getString("character");
        this.quote = data.getString("quote");
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
                .setDescription("\"" + quote + "\"\n-" + character)
                .setFooter("from " + anime);
    }

}
