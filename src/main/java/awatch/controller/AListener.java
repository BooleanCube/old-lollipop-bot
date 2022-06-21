package awatch.controller;

import awatch.model.*;
import awatch.model.Character;
import awatch.model.Question;

import java.util.ArrayList;


public interface AListener {

    void sendSearchAnime(ArrayList<Anime> animes);
    void sendSearchCharacter(Character character);
    void sendRandomQuote(Quote quote);
    void sendEpisodes(ArrayList<Episode> episodes);
    void sendCharacterList(ArrayList<Character> characters);
    void sendNews(ArrayList<Article> articles);
    void sendStatistics(Statistic statistics);
    void sendThemes(Themes themes);
    void sendRecommendation(Recommendation recommendation);
    void sendReview(Review review);
    void sendTop(ArrayList<Anime> top);
    void sendPopularAnime(ArrayList<Anime> popular);
    void sendLatest(ArrayList<Anime> latest);
    void sendRandomAnime(Anime random);
    void sendRandomGIF(GIF gif);
    void sendTrivia(Question question);


}
