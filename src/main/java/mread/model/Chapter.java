package mread.model;

import mread.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jsoup.nodes.Element;

import java.util.List;

public class Chapter implements ModelData {

    public User user;
    public int pageNumber = 1;
	public String title;
	public String url;
	public String publication;
	public List<String> pages;

    private static final String BASE_URL = "https://www.readm.org/";

    /**
     * Initialize variables in the constructor
     * @param title chapter title
     * @param url chapter url
     * @param publication chapter publication
     * @param pages chapter pages
     */
	public Chapter(String title, String url, String publication, List<String> pages) {
		super();
		this.title = ifNull(title);
		this.url = ifNull(url);
		this.publication = ifNull(publication);
		this.pages = pages;
	}

    /**
     * Compressed data into string
     * @return String representing chapter and its contents
     */
	@Override
	public String toString() {
		return "Chapter [title=" + title + ", url=" + url + ", publication=" + publication + ", pages=" + pages + "]";
	}

    /**
     * Checks if val equals null
     * @param val string
     * @return string
     */
	private String ifNull(String val) {
		return val == null ? "" : val;
	}

    /**
     * Parse the data from a document into the chapter object
     * @param document document to parse from
     */
    @Override
    public void parseData(Element document) {

    }

    /**
     * Compressed all data into an EmbedBuilder
     * (empty because i need an embedbuilder of all chapters not a singular chapter)
     * @return embedbuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setTitle(this.title, BASE_URL + this.url)
                .setDescription(this.publication + " ago")
                .setImage(BASE_URL + this.pages.get(0))
                .setFooter("Page " + pageNumber + "/" + pages.size());
    }

    /**
     * Get the chapter embed for a specific page
     * @param page page number
     * @return {@link EmbedBuilder} embed builder to send
     */
    public EmbedBuilder embedPage(int page) {
        return new EmbedBuilder()
                .setTitle(this.title, BASE_URL + this.url)
                .setDescription(this.publication + " ago")
                .setImage(BASE_URL + this.pages.get(page))
                .setFooter("Page " + pageNumber + "/" + pages.size());
    }

}
