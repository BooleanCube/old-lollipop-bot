package awatch;

import org.json.JSONObject;

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

}
