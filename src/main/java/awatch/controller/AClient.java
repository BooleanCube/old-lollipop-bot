package awatch.controller;

import threading.ThreadManagement;

import java.io.IOException;

public class AClient {

    private final AListener listener;

    public AClient(AListener listner) {
        this.listener = listner;
    }

    public void searchAnime(String query, boolean nsfw) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendSearchAnime(ALoader.loadAnime(query, nsfw));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void searchCharacter(String query) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendSearchCharacter(ALoader.loadCharacter(query));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void randomQuote() {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRandomQuote(ALoader.loadQuote());
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getEpisodes(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendEpisodes(ALoader.loadEpisodes(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getNews(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendNews(ALoader.loadNews(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getStatistics(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendStatistics(ALoader.loadStatistics(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getThemes(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendThemes(ALoader.loadThemes(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getRecommendation(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRecommendation(ALoader.loadRecommendations(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getReview(long id) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendReview(ALoader.loadReview(id));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getTop() {
        ThreadManagement.execute(() -> {
            try {
                listener.sendTop(ALoader.loadTop());
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void getLatest() {
        ThreadManagement.execute(() -> {
            try {
                listener.sendLatest(ALoader.loadLatest());
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void randomAnime(boolean nsfw) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRandomAnime(ALoader.loadRandom(nsfw));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    public void randomGIF(String type) {
        ThreadManagement.execute(() -> {
            try {
                listener.sendRandomGIF(ALoader.loadGIF(type));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

}
