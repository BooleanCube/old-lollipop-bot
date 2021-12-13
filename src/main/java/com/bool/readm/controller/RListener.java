package com.bool.readm.controller;

import java.util.List;

import com.bool.readm.model.Chapter;
import com.bool.readm.model.Manga;

public interface RListener {

	void setMangas(List<Manga> mangas);

	void setChapters(Manga manga);

	void setPages(Chapter chapter);
}
