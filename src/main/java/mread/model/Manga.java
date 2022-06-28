package mread.model;

import awatch.controller.AConstants;
import lollipop.pages.ChapterList;
import mread.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jsoup.nodes.Element;

import java.util.List;

public class Manga implements ModelData {

	public String title = "Unknown";
	public String art = "";
	public String url = "";
	public int chapter = 0;
	public double score = 0.0;

	// more details
    public String type = "";
    public int subscibers = 0;
    public int views = 0;
	public String status = "";
	public String summary = "";
	public String author = "";
    public String genres = "";
	public String authorUrl = "";
	public List<String> tags;
	public ChapterList chapters;
    public int rank;

    //MAL
    public String rating;
    public String titleJapanese;
    public int popularity;
    public String demographics;
    public String authors;

    private static final String BASE_URL = "https://www.readm.org";

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

    public Manga() {
        super();
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
        this.summary = doc.select("div[class=series-summary-wrapper]").text().replaceAll("SUMMARY", "").trim();
        this.genres = summary.substring(summary.indexOf("Genre")).split(" ", 2)[1];
        this.summary = summary.substring(0, summary.indexOf("Genre"));
        String[] infoTable = doc.select("div[class=media-meta]").select("table[class=ui unstackable single line celled table]").text().split(" ");
        this.type = infoTable[1];
        this.chapter = Integer.parseInt(infoTable[3]);
        this.subscibers = Integer.parseInt(infoTable[5].replaceAll(",",""));
        this.score = Double.parseDouble(infoTable[7]);
        this.views = Integer.parseInt(infoTable[9].replaceAll(",",""));
    }

    public void parseRankData(Element doc) {
        String[] infoTable = doc.select("div[class=media-meta]").select("table[class=ui single line celled unstackable table]").text().split(" ");
        this.rank = Integer.parseInt(infoTable[1]);
        this.type = infoTable[3];
        this.subscibers = Integer.parseInt(infoTable[5].replaceAll(",",""));
        this.score = Double.parseDouble(infoTable[7]);
        this.views = Integer.parseInt(infoTable[9].replaceAll(",",""));
        this.url = BASE_URL + doc.select("a").attr("href");
        this.title = doc.select("a").attr("title");
        this.art = BASE_URL + doc.select("img").attr("src");
    }

    public void parseLatestData(Element doc) {
        this.url = BASE_URL + doc.select("a").attr("href");
        this.title = doc.select("h2[class=truncate]").select("a").text();
        this.art = BASE_URL + doc.select("img[class=lazy-wide loaded]").attr("src");
        String pub = doc.select("span[class=date]").select("date").text();
        ChapterList list = new ChapterList();
        for(Element c : doc.select("ul[class=chapters]").select("li")) {
            Chapter chapter = new Chapter();
            chapter.publication = pub;
            chapter.title += c.select("a").text();
            chapter.url = BASE_URL + c.select("a").attr("href");
            list.chapters.add(chapter);
        }
        this.chapters = list;
    }

    public void parseMALData(DataObject data) {
        this.art = data.getObject("images").getObject("jpg").getString("image_url", "");
        this.status = data.getString("status", "");
        this.rating = data.getString("rating", "");
        this.score = data.getDouble("score", 0);
        this.summary = data.getString("synopsis", "");
        this.title = data.getString("title", "");
        this.titleJapanese = data.getString("title_japanese", "わからない");
        this.url = data.getString("url", "");
        this.rank = data.getInt("rank", Integer.MAX_VALUE);
        if(this.rank == 0) rank = Integer.MAX_VALUE;
        this.type = data.getString("type", "");
        this.chapter = data.getInt("chapters", 0);
        this.popularity = data.getInt("popularity", Integer.MAX_VALUE);
        if(this.popularity == 0) this.popularity = Integer.MAX_VALUE;
        StringBuilder sb = new StringBuilder();
        DataArray genres = data.getArray("genres");
        for(int i=0; i<genres.length(); i++) {
            DataObject genre = genres.getObject(i);
            sb.append("[").append(genre.getString("name", "Unknown")).append("](").append(genre.getString("url","")).append("), ");
        }
        this.genres = sb.toString().trim();
        if(this.genres.equals("")) this.genres = "Unknown";
        else this.genres = this.genres.substring(0, this.genres.length()-1);
        sb = new StringBuilder();
        DataArray demographics = data.getArray("demographics");
        for(int i=0; i<demographics.length(); i++) {
            DataObject demographic = demographics.getObject(i);
            sb.append("[").append(demographic.getString("name", "Unknown")).append("](").append(demographic.getString("url","")).append("), ");
        }
        this.demographics = sb.toString().trim();
        if(this.demographics.equals("")) this.demographics = "None";
        else this.demographics = this.demographics.substring(0, this.demographics.length()-1);
        sb = new StringBuilder();
        DataArray auths = data.getArray("authors");
        for(int i=0; i<Math.min(12,auths.length()); i++) {
            DataObject author = auths.getObject(i);
            sb.append("[").append(author.getString("name", "Unknown")).append("](").append(author.getString("url","")).append("), ");
        }
        this.authors = sb.toString().trim();
        if(this.authors.equals("")) this.authors = "Unknown";
        else this.authors = this.authors.substring(0, this.authors.length()-1);
    }

    /**
     * Compressed data into an EmbedBuilder
     * @return embedbuilder for discord bot developers
     */
    @Override
    public EmbedBuilder toEmbed() {
        if(this.summary.length() > 2000) this.summary = this.summary.substring(0, 1000) + "... [Read More!](" + this.url + ")";
        return new EmbedBuilder()
                .setAuthor(this.title, this.url)
                .setDescription(this.summary)
                .setImage(this.art)
                .addField("Type", this.type, true)
                .addField("Chapters", Integer.toString(this.chapter),true)
                .addField("Status", this.status,true)
                .addField("Score", this.score + "/10", true)
                .addField("Views", Integer.toString(this.views), true)
                .addField("Subscribers", Integer.toString(this.subscibers), true)
                .addField("Authors", this.author.isEmpty() ? "Unknown" : this.author,true)
                .addField("Tags", this.tags==null || tags.isEmpty() ? "None" : String.join(", ", this.tags), true)
                .addField("Genres", this.genres, false);
    }

    /**
     * Manga Embed with rank
     * @return EmbedBuilder
     */
    public EmbedBuilder toRankEmbed() {
        if(this.summary.length() > 2000) this.summary = this.summary.substring(0, 1000) + "... [Read More!](" + this.url + ")";
        return new EmbedBuilder()
                .setAuthor(this.title, this.url)
                .setImage(this.art)
                .addField("Rank", "#" + this.rank, false)
                .addField("Type", this.type, true)
                .addField("Score", this.score + "/10", true)
                .addField("Views", Integer.toString(this.views), true)
                .addField("Subscribers", Integer.toString(this.subscibers), true);
    }

    /**
     * MAL Manga Embed
     * @return EmbedBuilder
     */
    public EmbedBuilder toMALEmbed() {
        EmbedBuilder e = new EmbedBuilder()
                .setDescription(this.summary != null ? this.summary + " [Read More!](" + this.url + ")" : "[Read Here](" + this.url + ")")
                .setTitle(this.title + " (" + this.titleJapanese + ")")
                .addField("Type", this.type, true)
                .addField("Rating", this.rating, true)
                .addField("Score", this.score + "/10", true)
                .addField("Rank", "#"+this.rank, true)
                .addField("Popularity", "#"+this.popularity, true)
                .addField("Status", this.status, true)
                .addField("Chapter Count", this.chapter + " chapter(s)", true)
                .addField("Demographics", this.demographics, true)
                .addField("Genres", this.genres, true)
                .addField("Authors", this.authors, false);
        if(!this.art.equals("")) e.setImage(this.art);
        return e;
    }

}
