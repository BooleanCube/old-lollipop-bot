package mread.controller;

import java.util.Set;

import mread.model.Chapter;
import mread.model.Manga;
import threading.ThreadManagement;

public class RClient {

	private RListener listener;

	public RClient(RListener listener) {
		this.listener = listener;
	}

	// genre can be null
	public void browse(int page, String genre) {
        ThreadManagement.execute(() -> listener.sendMangas(RLoader.browse(page, genre)));
	}

	// search for manga by keyword
	public void search(String query) {
		ThreadManagement.execute(() -> listener.sendMangas(RLoader.search(query)));
	}

	// get chapters
	public void chapters(Manga manga) {
		ThreadManagement.execute(() -> listener.sendChapters(RLoader.getChapters(manga)));
	}

	// get pages
	public void pages(Chapter chapter) {
		ThreadManagement.execute(() -> listener.sendPages(RLoader.getPages(chapter)));
	}

	// get all genres
	public Set<String> genres() {
		return RConstants.getGenres().keySet();
	}
}
