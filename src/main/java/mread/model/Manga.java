package mread.model;

import awatch.controller.AConstants;
import lollipop.pages.ChapterList;
import mread.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.nodes.Element;

import java.util.List;

public class Manga implements ModelData {
	public String title;
	public String art;
	public String url;
	public int chapter;
	public double score;

	// more details
    public String type;
    public int subscibers;
    public int views;
	public String status;
	public String summary;
	public String author;
    public String genres;
	public String authorUrl;
	public List<String> tags;
	public ChapterList chapters;

    /**
     * Initialize variables in constructor
     * @param title manga title
     * @param url manga url
     * @param summary manga summary
     * @param art manga poster link
     * @param tags manga tags
     */
	public Manga(String title, String url, String summary, String art, List<String> tags) {
		super();

		this.title = ifNull(title);
		this.art = ifNull(art);
		this.url = ifNull(url);
		this.summary = ifNull(summary);
		this.tags = tags;
	}

    /**
     * Compressed data into a string
     * @return string
     */
	@Override
	public String toString() {
		return "Manga [title=" + title + ", art=" + art + ", url=" + url + ", chapter=" + chapter + ", rating=" + score
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
     * Parse all of the data inside the document
     * @param doc document to parse data from
     */
    @Override
    public void parseData(Element doc) {
        this.author = doc.select("div[class=first_and_last]").text().replaceAll(" Author", ", Author").replaceAll(" Artist", ", Artist");
        this.status = doc.select("span[class=series-status aqua]").text();
        if(this.status.equalsIgnoreCase("ongoing")) this.status = "Ongoing";
        if(this.status.equalsIgnoreCase("completed")) this.status = "Completed";
        if(this.status.equals("")) this.status = "Unknown";
        this.summary = doc.select("div[class=series-summary-wrapper]").text();
        this.genres = summary.substring(summary.indexOf("Genre")).split(" ", 2)[1];
        this.summary = summary.substring(0, summary.indexOf("Genre"));
        String[] infoTable = doc.select("div[class=media-meta]").select("table[class=ui unstackable single line celled table]").text().split(" ");
        this.type = infoTable[1];
        this.chapter = Integer.parseInt(infoTable[3]);
        this.subscibers = Integer.parseInt(infoTable[5]);
        this.score = Double.parseDouble(infoTable[7]);
        this.views = Integer.parseInt(infoTable[9].replaceAll(",",""));
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
                .addField("Type", this.type, true)
                .addField("Chapters", Integer.toString(this.chapter),true)
                .addField("Status", this.status,true)
                .addField("Score", this.score + "/10", true)
                .addField("Views", Integer.toString(this.views), true)
                .addField("Subscribers", Integer.toString(this.subscibers), true)
                .addField("Authors",this.author,true)
                .addField("Tags", String.join(", ", this.tags), true)
                .addField("Genres", this.genres, false);
    }

}
