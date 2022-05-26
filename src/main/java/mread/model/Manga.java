package mread.model;

import awatch.controller.AConstants;
import mread.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public class Manga implements ModelData {
	public String title;
	public String art;
	public String url;
	public String chapter;
	public String rating;

	// more details
	public String status;
	public String summary;
	public String author;
	public String authorUrl;
	public List<String> tags;
	public List<Chapter> chapters;

    /**
     * Initialize variables in constructor
     * @param title manga title
     * @param url manga url
     * @param summary manga summary
     * @param rating manga rating
     * @param art manga poster link
     * @param tags manga tags
     */
	public Manga(String title, String url, String summary, String rating, String art, List<String> tags) {
		super();

		this.title = ifNull(title);
		this.art = ifNull(art);
		this.url = ifNull(url);
		this.rating = ifNull(rating);
		this.summary = ifNull(summary);
		this.tags = tags;
	}

    /**
     * Compressed data into a string
     * @return string
     */
	@Override
	public String toString() {
		return "Manga [title=" + title + ", art=" + art + ", url=" + url + ", chapter=" + chapter + ", rating=" + rating
				+ ", status=" + status + ", summary=" + summary + ", author=" + author + ", authorUrl=" + authorUrl
				+ ", tags=" + tags + ", chapters=" + chapters + "]";
	}

    /**
     * Checks if val is null
     * @param val string
     * @return string
     */
	private String ifNull(String val) {
		return val == null ? "" : val;
	}

    /**
     * Compressed data into an EmbedBuilder
     * @return embedbuilder for discord bot developers
     */
    @Override
    public EmbedBuilder toEmbed() {
        if(this.summary.length() > 2000) this.summary = this.summary.substring(0, 1000) + "... [Read More!](" + this.url + ")";
        return new EmbedBuilder()
                .setAuthor(this.title, AConstants.readmAPI+this.url)
                .setDescription(this.summary.replaceAll("SUMMARY", "").trim())
                .setImage(AConstants.readmAPI +this.art)
                .addField("Authors",this.author,true)
                .addField("Chapters",this.chapter,true)
                .addField("Rating", this.rating, true)
                .addField("Status", this.status,true)
                .addField("Tags", String.join(", ", this.tags), false);
    }

}
