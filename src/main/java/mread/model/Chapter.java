package mread.model;

import java.util.List;

public class Chapter {
	public String title;
	public String url;
	public String publication;
	public List<String> pages;

	public Chapter(String title, String url, String publication, List<String> pages) {
		super();
		this.title = ifNull(title);
		this.url = ifNull(url);
		this.publication = ifNull(publication);
		this.pages = pages;
	}

	@Override
	public String toString() {
		return "Chapter [title=" + title + ", url=" + url + ", publication=" + publication + ", pages=" + pages + "]";
	}

	private String ifNull(String val) {
		return val == null ? "" : val;
	}
}
