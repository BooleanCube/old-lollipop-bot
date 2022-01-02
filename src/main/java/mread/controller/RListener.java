package mread.controller;

import java.util.List;

import mread.model.Chapter;
import mread.model.Manga;

public interface RListener {

	void setMangas(List<Manga> mangas);

	void setChapters(Manga manga);

	void setPages(Chapter chapter);
}
