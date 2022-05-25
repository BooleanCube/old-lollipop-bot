package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Arrays;

/**
 * Article Model
 */
public class Article implements ModelData {

    public String url;
    public String title;
    public String date;
    public String author;
    public String authorUrl;
    public String forum;
    public String image;
    public String desc;
    public int comments;

    public Article() {}

    /**
     * Returns a string with all the data
     * @return string
     */
    public String toString() {
        return Arrays.toString(new String[]{url, title, date, author, authorUrl, forum, image, desc, Integer.toString(comments)});
    }

    /**
     * Parses all the data
     * @param data
     */
    @Override
    public void parseData(DataObject data) {
        this.author = data.getString("author_username", "");
        this.authorUrl = data.getString("author_url", "");
        this.url = data.getString("url", "");
        this.title = data.getString("title", "");
        this.comments = data.getInt("comments", 0);
        this.date = data.getString("date", "");
        this.desc = data.getString("excerpt", "");
        this.forum = data.getString("forum_url", "");
        this.image = data.getObject("images").getObject("jpg").getString("image_url", "");
    }

    /**
     * Parses all the data
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
                .setAuthor(this.author,this.authorUrl)
                .setTitle(this.title, this.url)
                .setDescription(this.desc + "\n[Forum Page](" + this.forum + ")")
                .setThumbnail(this.image.equals("") ? "https://t4.ftcdn.net/jpg/01/32/42/17/360_F_132421793_uTO9DQFdFeyETi3ZPqeSy9nh4ec0shOd.jpg" : this.image)
                .addField("----------------------------------------------------------------", this.date + " | " + this.comments + " comments", false);
    }

}
