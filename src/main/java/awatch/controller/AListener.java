package awatch.controller;

import awatch.model.*;
import awatch.model.Character;
import lollipop.pages.EpisodeList;

import java.util.ArrayList;


public interface AListener {

    void sendSearchAnime(ArrayList<Anime> animes);
    void sendSearchCharacter(Character character);
    void sendRandomQuote(Quote quote);
    void sendEpisodes(ArrayList<Episode> episodes);
    void sendNews(ArrayList<Article> articles);
    void sendStatistics(Statistic statistics);
    void sendThemes(Themes themes);
    void sendRecommendation(Recommendation recommendation);
    void sendReview(Review review);
    void sendTop(ArrayList<Anime> top);
    void sendLatest(ArrayList<Anime> latest);
    void sendRandomAnime(Anime random);
    void sendRandomGIF(GIF gif);


}
