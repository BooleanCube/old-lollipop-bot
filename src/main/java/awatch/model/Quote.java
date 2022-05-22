package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

public class Quote implements ModelData {

    public String quote;
    public String character;
    public String anime;

    public Quote() {}

    @Override
    public void parseData(DataObject data) {
        this.anime = data.getString("anime");
        this.character = data.getString("character");
        this.quote = data.getString("quote");
    }

    @Override
    public void parseData(DataArray data) {
        // empty
    }

    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setDescription("\"" + quote + "\"\n-" + character)
                .setFooter("from " + anime);
    }

}
