package awatch.model;

import awatch.ModelData;
import lollipop.Cache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.IOException;

public class GIF implements ModelData {

    public String url;

    public GIF() {}

    @Override
    public void parseData(DataObject data) {
        this.url = data.getString("response");
        try {
            Cache.addGifToCache(this.url);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    public void parseData(DataArray data) {
        //empty
    }

    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setImage(url);
    }

}
