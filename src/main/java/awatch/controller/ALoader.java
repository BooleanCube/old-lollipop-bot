package awatch.controller;

import awatch.model.*;
import awatch.model.Character;
import lollipop.Secret;
import awatch.model.Question;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Reads all data from Jikan REST API
 */
public class ALoader {

    // Anime Cache
    public static HashMap<String, ArrayList<Anime>> animeCache = new HashMap<>();
    public static HashMap<String, ArrayList<Character>> characterCache = new HashMap<>();

    /**
     * Load Animes from a certain search query
     * @param query anime name
     * @param nsfw nsfw allowed
     * @return arraylist of animes
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Anime> loadAnime(String query, boolean nsfw) throws IOException {
        if(animeCache.containsKey(query)) return animeCache.get(query);
        String extension = !nsfw ? "&sfw=true" : "";
        URL web = new URL(AConstants.v4API+"/anime?q=" + query.replaceAll(" ", "%20") + extension);
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Anime> animes = new ArrayList<>();
        DataArray arr;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject result = arr.getObject(i);
            Anime anime = new Anime();
            anime.parseData(result);
            animes.add(anime);
        }
        animeCache.put(query, animes);
        return animes;
    }

    /**
     * Load a character given query
     * @param query character name
     * @return list of characters
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Character> loadCharacter(String query) throws IOException {
        URL web = new URL(AConstants.v4API+"/characters?q=" + query.replaceAll(" ", "%20"));
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Character> characters = new ArrayList<>();
        DataArray arr = null;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject result = arr.getObject(i);
            Character character = new Character();
            character.parseData(result);
            characters.add(character);
        }
        return characters;
    }

    /**
     * Load the user profile
     * @param query String query for username
     * @return user with parsed info
     * @throws IOException for BufferedReader
     */
    public static User loadSearchUser(String query) throws IOException {
        URL web = new URL(AConstants.v4API + "/users/" + query + "/full");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        User user = new User();
        user.parseData(data.getObject("data"));
        return user;
    }

    /**
     * Load the character components
     * @param character character to get components
     * @return character with components filled
     * @throws IOException for BufferedReader
     */
    public static Character loadCharacterInfo(Character character) throws IOException {
        long id = character.malID;
        URL web = new URL(AConstants.v4API + "/characters/" + id + "/full");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        character.parseComponents(data.getObject("data"));
        return character;
    }

    /**
     * Load a random anime related quote
     * @return random quote
     * @throws IOException for BufferedReader
     */
    public static Quote loadQuote() throws IOException {
        URL web = new URL(AConstants.quoteAPI);
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Quote quote = new Quote();
        quote.parseData(data);
        return quote;
    }

    /**
     * Loads the episodes of the given anime
     * @param id MAL id
     * @return arraylist of episodes
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Episode> loadEpisodes(long id) throws IOException {
        URL web = new URL(AConstants.v4API+"/anime/" + id + "/episodes");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Episode> episodes = new ArrayList<>();
        DataArray arr;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            Episode episode = new Episode();
            episode.parseData(res);
            episodes.add(episode);
        }
        return episodes;
    }

    /**
     * Loads the character list of the given anime
     * @param id MAL id
     * @return arraylist of characters
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Character> loadCharacterList(long id) throws IOException {
        URL web = new URL(AConstants.v4API+"/anime/" + id + "/characters");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Character> characters = new ArrayList<>();
        DataArray arr;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            Character character = new Character();
            character.parseDataList(res);
            characters.add(character);
        }
        return characters;
    }

    /**
     * Loads recent news from the given anime
     * @param id MAL id
     * @return arraylist of articles
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Article> loadNews(long id) throws IOException {
        URL web = new URL(AConstants.v4API+"/anime/" + id + "/news");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Article> articles = new ArrayList<>();
        DataArray arr;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            Article article = new Article();
            article.parseData(res);
            articles.add(article);
        }
        return articles;
    }

    /**
     * Load statistics from the given anime
     * @param id MAL id
     * @return statistic
     * @throws IOException for BufferedReader
     */
    public static Statistic loadStatistics(long id) throws IOException {
        URL web = new URL(AConstants.v4API+"/anime/" + id + "/statistics");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Statistic stats = new Statistic();
        stats.parseData(data);
        return stats;
    }

    /**
     * Loads themes from the given anime
     * @param id MAL id
     * @return themes
     * @throws IOException for BufferedReader
     */
    public static Themes loadThemes(long id) throws IOException {
        URL web = new URL(AConstants.v4API+"/anime/" + id + "/themes");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Themes themes = new Themes();
        themes.parseData(data);
        return themes;
    }

    /**
     * Loads recommended animes from the given anime
     * @param id MAL id
     * @return recommendation list
     * @throws IOException for BufferedReader
     */
    public static Recommendation loadRecommendations(long id) throws IOException {
        URL web = new URL(AConstants.v4API+"/anime/" + id + "/recommendations");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Recommendation recommendation = new Recommendation();
        recommendation.parseData(data);
        return recommendation;
    }

    /**
     * Loads the top review from the given anime
     * @param id MAL id
     * @return review
     * @throws IOException for BufferedReader
     */
    public static Review loadReview(long id) throws IOException {
        URL web = new URL(AConstants.v4API+"/anime/" + id + "/reviews");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Review review = new Review();
        review.parseData(data);
        return review;
    }

    /**
     * Load the top animes ranked in terms of score
     * @return arraylist of anime
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Anime> loadTop() throws IOException {
        URL web = new URL(AConstants.v4API+"/top/anime");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Anime> animes = new ArrayList<>();
        DataArray arr;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        int size = Math.min(20, arr.length());
        for(int i=0; i<size; i++) {
            DataObject result = arr.getObject(i);
            Anime anime = new Anime();
            anime.parseData(result);
            animes.add(anime);
        }
        return animes;
    }

    /**
     * Load the top animes ranked in terms of popularity
     * @return arraylist of anime
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Anime> loadPopular() throws IOException {
        URL web = new URL(AConstants.v4API+"/top/anime?filter=bypopularity");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Anime> animes = new ArrayList<>();
        DataArray arr;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        int size = Math.min(20, arr.length());
        for(int i=0; i<size; i++) {
            DataObject result = arr.getObject(i);
            Anime anime = new Anime();
            anime.parseData(result);
            animes.add(anime);
        }
        return animes;
    }

    /**
     * Load the latest animes of the season
     * @return arraylist of animes
     * @throws IOException for BufferedReader
     */
    public static ArrayList<Anime> loadLatest() throws IOException {
        URL web = new URL(AConstants.v4API+"/seasons/now");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        ArrayList<Anime> animes = new ArrayList<>();
        DataArray arr;
        try {
            arr = data.getArray("data");
        } catch(Exception e) { return null; }
        int size = Math.min(20, arr.length());
        for(int i=0; i<size; i++) {
            DataObject result = arr.getObject(i);
            Anime anime = new Anime();
            anime.parseData(result);
            animes.add(anime);
        }
        return animes;
    }

    /**
     * Load a random anime
     * @param nsfw nsfw allowed
     * @return anime
     * @throws IOException for BufferedReader
     */
    public static Anime loadRandomAnime(boolean nsfw) throws IOException {
        String extension = !nsfw ? "?sfw" : "";
        URL web = new URL(AConstants.v4API+"/random/anime" + extension);
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Anime anime = new Anime();
        DataObject result;
        try {
            result = data.getObject("data");
        } catch(Exception e) { return null; }
        anime.parseData(result);
        return anime;
    }

    /**
     * Load a random anime
     * @param nsfw nsfw allowed
     * @return anime
     * @throws IOException for BufferedReader
     */
    public static Character loadRandomCharacter(boolean nsfw) throws IOException {
        String extension = !nsfw ? "?sfw" : "";
        URL web = new URL(AConstants.v4API+"/random/characters" + extension);
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Character character = new Character();
        DataObject result;
        try {
            result = data.getObject("data");
        } catch(Exception e) { return null; }
        character.parseData(result);
        return character;
    }

    /**
     * Load a random anime related GIF
     * @return GIF
     * @throws IOException for BufferedReader
     */
    public static GIF loadGIF() throws IOException {
        String[] types = {"alarm", "amazing", "ask", "baka", "bite", "blush", "blyat", "boop", "clap", "coffee", "confused", "cry", "cuddle", "cute", "dance", "destroy", "die", "disappear", "dodge", "error", "facedesk", "facepalm", "fbi", "fight", "happy", "hide", "highfive", "hug", "kill", "kiss", "laugh", "lick", "lonely", "love", "mad", "money", "nom", "nosebleed", "ok", "party", "pat", "peek", "poke", "pout", "protect", "puke", "punch", "purr", "pusheen", "run", "salute", "scared", "scream", "shame", "shocked", "shoot", "shrug", "sip", "sit", "slap", "sleepy", "smile", "smoke", "smug", "spin", "stare", "stomp", "tickle", "trap", "triggered", "uwu", "wasted", "wave", "wiggle", "wink", "yeet"};
        String type = types[(int)(Math.random()*types.length)];
        URL web = new URL(AConstants.kawaiiAPI + "/" + type + "/token=" + Secret.KAWAIIAPITOKEN + "/");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        GIF gif = new GIF();
        gif.parseData(data);
        return gif;
    }

    /**
     * Load a random question for trivia
     * @return anime
     * @throws IOException for BufferedReader
     */
    public static Question loadTrivia(HashSet<String> available) throws IOException {
        URL web = new URL(AConstants.v4API+"/random/anime?sfw");
        HttpsURLConnection con = configureConnection(web);
        BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
        DataObject data = DataObject.fromJson(bf.readLine());
        Anime anime = new Anime();
        DataObject result;
        try {
            result = data.getObject("data");
        } catch(Exception e) { return null; }
        anime.parseData(result);
        Question question = new Question(anime, new ArrayList<>(List.of(anime.title)));
        question.generateOptions(available, 3);
        Collections.shuffle(question.options);
        return question;
    }

    /**
     * Declare the HttpsURLConnection and configure for REST API
     * @param web rest api url object
     * @return {@link HttpsURLConnection} for the REST API
     * @throws IOException for declaring and configuring {@link HttpsURLConnection}
     */
    public static HttpsURLConnection configureConnection(URL web) throws IOException{
        HttpsURLConnection con = (HttpsURLConnection) web.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        con.setConnectTimeout(5000); // Sets Connection Timeout to 5 seconds
        con.setReadTimeout(5000); // Sets Read Timeout to 5 seconds
        return con;
    }

}
