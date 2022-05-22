package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

public class Episode implements ModelData {

    public String url;
    public String title;
    public Episode(String u, String t) {
        url = u;
        title = t;
    }
    public Episode() {}

    @Override
    public void parseData(DataObject data) {
        this.url = data.getString("url", "");
        this.title = data.getString("title_romanji", data.getString("title", ""));
    }

    @Override
    public void parseData(DataArray data) {
        // empty
    }

    @Override
    public EmbedBuilder toEmbed() {
        //empty
        return null;
    }

}
