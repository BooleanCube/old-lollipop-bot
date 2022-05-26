package mread.controller;

import java.util.List;

import mread.model.Chapter;
import mread.model.Manga;

public interface RListener {

    /**
     * Sends a manga list to the listener
     * @param mangas manga list
     */
	void sendMangas(List<Manga> mangas);

    /**
     * Sends a list of chapters
     * @param manga manga containing chapters
     */
	void sendChapters(Manga manga);

    /**
     * Sends a list of pages
     * @param chapter chapter containing pages
     */
	void sendPages(Chapter chapter);

}
