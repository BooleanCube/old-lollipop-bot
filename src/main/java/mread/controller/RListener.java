package mread.controller;

import java.util.ArrayList;

import mread.model.Chapter;
import mread.model.Manga;

public interface RListener {

    /**
     * Sends a manga list to the listener
     * @param mangas list of mangas
     */
	void sendMangas(ArrayList<Manga> mangas);

    /**
     * Sends a list of chapters
     * @param chapters list of chapters
     */
	void sendChapters(ArrayList<Chapter> chapters);

    /**
     * Sends a list of pages
     * @param pages list of pages
     */
	void sendPages(ArrayList<String> pages);

}
