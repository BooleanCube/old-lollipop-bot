package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Arrays;

/**
 * Statistic Model
 */
public class Statistic implements ModelData {
    public long watching;
    public long completed;
    public long onHold;
    public long dropped;
    public long planToWatch;
    public long total;

    public Statistic() {}

    /**
     * Returns a string with all of the data
     * @return string
     */
    public String toString() {
        return Arrays.toString(new long[]{watching, completed, onHold, dropped, planToWatch, total});
    }

    /**
     * Parses all of the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        DataObject result = null;
        try {
            result = data.getObject("data");
        } catch(Exception e) { return; }
        this.watching = result.getLong("watching", -1);
        this.completed = result.getLong("completed", -1);
        this.onHold = result.getLong("on_hold", -1);
        this.dropped = result.getLong("dropped", -1);
        this.planToWatch = result.getLong("plan_to_watch", -1);
        this.total = result.getLong("total", -1);
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
                .addField("Watching", String.valueOf(this.watching), true)
                .addField("Completed", String.valueOf(this.completed), true)
                .addField("On Hold", String.valueOf(this.onHold), true)
                .addField("Dropped", String.valueOf(this.dropped), true)
                .addField("Plan To Watch", String.valueOf(this.planToWatch), true)
                .setFooter("Total: " + this.total);
    }

}
