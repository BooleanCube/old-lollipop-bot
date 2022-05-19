package awatch.models;

import net.dv8tion.jda.api.EmbedBuilder;

public class Review {

    public String authorName;
    public String authorIcon;
    public String authorUrl;
    public String url;
    public String details;
    public int votes = 0;
    public int score = 0;

    public EmbedBuilder getEmbed() {
        return new EmbedBuilder()
                .setAuthor(authorName, authorUrl, authorIcon)
                .setTitle("Top Review")
                .setDescription(
                        details.length()>=1970 ?
                                details.substring(0,1960) + "...\n[Read More!](" + url + ")" :
                                details + "\n[Read More!](" + url + ")" )
                .addField("Votes", Integer.toString(votes), true)
                .addField("Score", Integer.toString(score), true);
    }

}
