package awatch;

import awatch.models.Character;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CParser {

    public static Character parseData(JSONObject data) {
        Character character = new Character();
        if(data.getJSONArray("results").length() == 0) return null;
        JSONObject firstResult = data.getJSONArray("results").getJSONObject(0);

        character.art = firstResult.getString("image_url");
        character.name = firstResult.getString("name");
        if(firstResult.getJSONArray("manga").length() > 0) character.manga = "[" + firstResult.getJSONArray("manga").getJSONObject(0).get("name") + "](" + firstResult.getJSONArray("manga").getJSONObject(0).get("url") + ")";
        character.malID = firstResult.getInt("mal_id");
        if(firstResult.getJSONArray("alternative_names").length() > 0)character.alternativeNames = firstResult.getJSONArray("alternative_names").join(", ");
        character.url = firstResult.getString("url");
        if(firstResult.getJSONArray("anime").length() > 0)character.anime = "[" + firstResult.getJSONArray("anime").getJSONObject(0).get("name") + "](" + firstResult.getJSONArray("anime").getJSONObject(0).get("url") + ")";
        return character;
    }

    public static String getRandomPictureChar(JSONObject data) {
        if(data.getJSONArray("data").length() == 0) return null;
        JSONArray urlData = data.getJSONArray("data");
        ArrayList<String> urls = new ArrayList<>();
        for(int i=0; i<urlData.length(); i++) urls.add(urlData.getJSONObject(i).getJSONObject("jpg").getString("image_url"));
        return urls.get((int)(Math.random()*urls.size()));
    }

    public static String getRandomPicture(JSONObject data) {
        if(data.getJSONArray("data").length() == 0) return null;
        JSONArray urlData = data.getJSONArray("data");
        ArrayList<String> urls = new ArrayList<>();
        for(int i=0; i<urlData.length(); i++) urls.add(urlData.getJSONObject(i).getJSONObject("jpg").getString("image_url"));
        return urls.get((int)(Math.random()*urls.size()));
    }

}
