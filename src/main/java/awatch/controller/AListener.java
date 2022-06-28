package awatch.controller;

import awatch.model.*;
import awatch.model.Character;
import awatch.model.Question;

import java.util.ArrayList;

public interface AListener {

    void sendSearchAnime(ArrayList<Anime> animes);
    void sendSearchCharacter(ArrayList<Character> character);
    void sendSearchUser(User user);
    void sendCharacterAnimes(Character character);
    void sendCharacterMangas(Character character);
    void sendCharacterVoices(Character character);
    void sendRandomQuote(Quote quote);
    void sendEpisodes(ArrayList<Episode> episodes);
    void sendCharacterList(ArrayList<Character> characters);
    void sendNews(ArrayList<Article> articles);
    void sendStatistics(Statistic statistics);
    void sendThemes(Themes themes);
    void sendRecommendation(Recommendation recommendation);
    void sendReview(Review review);
    void sendTopAnime(ArrayList<Anime> top);
    void sendPopularAnime(ArrayList<Anime> popular);
    void sendLatestAnime(ArrayList<Anime> latest);
    void sendRandomAnime(Anime random);
    void sendRandomCharacter(Character random);
    void sendRandomGIF(GIF gif);
    void sendTrivia(Question question);

}
