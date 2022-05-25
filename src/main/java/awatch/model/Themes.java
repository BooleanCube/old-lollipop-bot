package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Arrays;

/**
 * Themes Model
 */
public class Themes implements ModelData {

    public StringBuilder opening;
    public StringBuilder ending;

    public Themes() {
        opening = new StringBuilder();
        ending = new StringBuilder();
    }

    /**
     * Returns a string of all the data
     * @return string
     */
    public String toString() {
        return Arrays.toString(new String[]{opening.toString(), ending.toString()});
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        DataObject res = null;
        try {
            res = data.getObject("data");
            DataArray arr = null;
            arr = res.getArray("openings");
            for(int i=0; i<arr.length(); i++) {
                String result = arr.getString(i, "No opening themes found!");
                opening.append(result).append("\n");
            }
            arr = res.getArray("endings");
            for(int i=0; i<arr.length(); i++) {
                String result = arr.getString(i, "No ending themes found!");
                ending.append(result).append("\n");
            }
        } catch(Exception e) { return; }
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
     * Compresses the data into an EmbedBuilder
     * @return embedbuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setTitle("Anime Themes")
                .addField("Openings:", opening.toString(), true)
                .addField("Endings:", ending.toString(), true);
    }

}
