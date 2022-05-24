package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

public class Recommendation implements ModelData {

    public StringBuilder recommendationList;

    public Recommendation() {
        recommendationList = new StringBuilder();
    }

    @Override
    public void parseData(DataObject data) {
        DataArray arr = null;
        try {
            arr = data.getArray("data");
            int size = Math.min(10, arr.length());
            for(int i=0; i<size; i++) {
                DataObject res = arr.getObject(i);
                String title = res.getObject("entry").getString("title", "Title");
                String url = res.getObject("entry").getString("url", "https://myanimelist.net/");
                recommendationList.append("#").append(i+1).append(" - [").append(title).append("](").append(url).append(")\n");
            }
        } catch(Exception e) {}
    }

    @Override
    public void parseData(DataArray data) {
        // empty
    }

    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setTitle("Anime Recommendations")
                .setDescription(recommendationList);
    }

}
