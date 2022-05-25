package awatch.controller;

import threading.ThreadManagement;

import java.io.IOException;

/**
 * Access the threading and ALoader which retrieves data from the APIs
 */
public class AClient {

    private final AListener listener;

    /**
     * Initialize AListener in constructor
     * @param listener
     */
    public AClient(AListener listener) {
        this.listener = listener;
    }

    /**
     * Runs a thread to make a search anime call
     * @param query
     * @param nsfw
     */
    public void searchAnime(String query, boolean nsfw) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendSearchAnime(ALoader.loadAnime(query, nsfw));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to make a search character call
     * @param query
     */
    public void searchCharacter(String query) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendSearchCharacter(ALoader.loadCharacter(query));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to make a random quote call
     */
    public void randomQuote() {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRandomQuote(ALoader.loadQuote());
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get episodes of an anime
     * @param id
     */
    public void getEpisodes(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendEpisodes(ALoader.loadEpisodes(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get news of an anime
     * @param id
     */
    public void getNews(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendNews(ALoader.loadNews(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get the statistics of an anime
     * @param id
     */
    public void getStatistics(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendStatistics(ALoader.loadStatistics(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get the themes of an anime
     * @param id
     */
    public void getThemes(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendThemes(ALoader.loadThemes(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get the recommendations for an anime
     * @param id
     */
    public void getRecommendation(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRecommendation(ALoader.loadRecommendations(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get the top review of an anime
     * @param id
     */
    public void getReview(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendReview(ALoader.loadReview(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get the top 25 animes ranked in terms of score
     */
    public void getTop() {
        ThreadManagement.execute(() -> {
            try {
                listener.sendTop(ALoader.loadTop());
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get the latest animes of the season
     */
    public void getLatest() {
        ThreadManagement.execute(() -> {
            try {
                listener.sendLatest(ALoader.loadLatest());
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get a randomly chosen anime from MALs database
     * @param nsfw
     */
    public void randomAnime(boolean nsfw) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRandomAnime(ALoader.loadRandom(nsfw));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    /**
     * Runs a thread to get a randomly chosen anime related GIF
     */
    public void randomGIF() {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRandomGIF(ALoader.loadGIF());
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

}
