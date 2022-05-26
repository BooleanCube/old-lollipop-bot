package mread.model;

import mread.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public class Chapter implements ModelData {
	public String title;
	public String url;
	public String publication;
	public List<String> pages;

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
     * @return
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
     * Compressed all data into an EmbedBuilder
     * (empty because i need an embedbuilder of all chapters not a singular chapter)
     * @return embedbuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        // empty
        return null;
    }

}
