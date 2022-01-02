package mread.model;

import java.util.List;

public class Manga {
	public String title;
	public String art;
	public String url;
	public String chapter;
	public String rating;

	/* more details */
	public String status;
	public String summary;
	public String author;
	public String authorUrl;
	public List<String> tags;
	public List<Chapter> chapters;

	public Manga(String title, String url, String summary, String rating, String art, List<String> tags) {
		super();

		this.title = ifNull(title);
		this.art = ifNull(art);
		this.url = ifNull(url);
		this.rating = ifNull(rating);
		this.summary = ifNull(summary);
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Manga [title=" + title + ", art=" + art + ", url=" + url + ", chapter=" + chapter + ", rating=" + rating
				+ ", status=" + status + ", summary=" + summary + ", author=" + author + ", authorUrl=" + authorUrl
				+ ", tags=" + tags + ", chapters=" + chapters + "]";
	}

	private String ifNull(String val) {
		return val == null ? "" : val;
	}
}
