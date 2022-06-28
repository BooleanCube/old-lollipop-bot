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
     * Sends a popular manga list to the listener
     * @param popular list of mangas
     */
    void sendPopularManga(ArrayList<Manga> popular);

    /**
     * Sends a top manga list to the listener
     * @param top list of mangas
     */
    void sendTopManga(ArrayList<Manga> top);

    /**
     * Sends a latest manga list to the listener
     * @param latest list of mangas
     */
    void sendLatestManga(ArrayList<Manga> latest);

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

    /**
     * Sends a random manga from MAL
     * @param manga random manga
     */
    void sendRandomManga(Manga manga);

}
