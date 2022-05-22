package awatch;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

public interface ModelData {

    public void parseData(DataObject data);
    public void parseData(DataArray data);
    public EmbedBuilder toEmbed();

}
