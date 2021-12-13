package awatch;

import org.json.JSONObject;

public class AParser {

    public static Anime parseData(JSONObject data) {
        Anime anime = new Anime();
        if(data.getJSONArray("results").length() == 0) return null;
        JSONObject firstResult = data.getJSONArray("results").getJSONObject(0);
        anime.art = firstResult.getString("image_url");
        anime.malID = firstResult.getInt("mal_id");
        anime.status = !firstResult.getBoolean("airing") ? "Not airing" : "Airing";
        anime.rating = firstResult.getString("rated");
        anime.score = firstResult.getInt("score");
        anime.summary = firstResult.getString("synopsis");
        anime.title = firstResult.getString("title");
        anime.url = firstResult.getString("url");
        anime.episodeCount = firstResult.getInt("episodes");
        return anime;
    }

}
