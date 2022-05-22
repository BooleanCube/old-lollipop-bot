package mread.controller;

import java.util.List;

import mread.model.Chapter;
import mread.model.Manga;

public interface RListener {

	void sendMangas(List<Manga> mangas);

	void sendChapters(Manga manga);

	void sendPages(Chapter chapter);
}
