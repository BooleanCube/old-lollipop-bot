package mread;

import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.nodes.Element;

public interface ModelData {

    void parseData(Element document);
    EmbedBuilder toEmbed();

}
