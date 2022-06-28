package mread.controller;

import java.util.Set;

import mread.model.Chapter;
import mread.model.Manga;
import threading.ThreadManagement;

public class RClient {

	private final RListener listener;

	public RClient(RListener listener) {
		this.listener = listener;
	}

	// genre can be null
	public void browseManga(int page, String genre) {
        ThreadManagement.execute(() -> listener.sendMangas(RLoader.browseManga(page, genre)));
	}

	// search for manga by keyword
	public void searchManga(String query) {
		ThreadManagement.execute(() -> listener.sendMangas(RLoader.searchManga(query)));
	}

    // get popular mangas
    public void getPopularManga() {
        ThreadManagement.execute(() -> listener.sendPopularManga(RLoader.getPopularManga()));
    }

    // get top rated mangas
    public void getTopManga() {
        ThreadManagement.execute(() -> listener.sendTopManga(RLoader.getTopManga()));
    }

    // get latest mangas
    public void getLatestManga() {
        ThreadManagement.execute(() -> listener.sendLatestManga(RLoader.getLatestManga()));
    }

    public void randomManga(boolean nsfw) {
        ThreadManagement.execute(() -> listener.sendRandomManga(RLoader.getRandomManga(nsfw)));
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
