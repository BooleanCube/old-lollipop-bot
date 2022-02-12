package awatch;

import awatch.models.Character;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class CParser {

    public static Character parseData(DataObject data) {
        Character character = new Character();
        if(data.getArray("results").length() == 0) return null;
        DataObject firstResult = data.getArray("results").getObject(0);

        character.art = firstResult.getString("image_url");
        character.name = firstResult.getString("name");
        if(firstResult.getArray("manga").length() > 0) character.manga = "[" + firstResult.getArray("manga").getObject(0).get("name") + "](" + firstResult.getArray("manga").getObject(0).get("url") + ")";
        character.malID = firstResult.getInt("mal_id");
        if(firstResult.getArray("alternative_names").length() > 0) character.alternativeNames = firstResult.getArray("alternative_names").toList().stream().map(a -> Objects.toString(a, null)).collect(Collectors.joining(", "));
        character.url = firstResult.getString("url");
        if(firstResult.getArray("anime").length() > 0)character.anime = "[" + firstResult.getArray("anime").getObject(0).get("name") + "](" + firstResult.getArray("anime").getObject(0).get("url") + ")";
        return character;
    }

    public static String getRandomPictureChar(DataObject data) {
        if(data.getArray("data").length() == 0) return null;
        DataArray urlData = data.getArray("data");
        ArrayList<String> urls = new ArrayList<>();
        for(int i=0; i<urlData.length(); i++) urls.add(urlData.getObject(i).getObject("jpg").getString("image_url"));
        return urls.get((int)(Math.random()*urls.size()));
    }

    public static String getRandomPicture(DataObject data) {
        if(data.getArray("data").length() == 0) return null;
        DataArray urlData = data.getArray("data");
        ArrayList<String> urls = new ArrayList<>();
        for(int i=0; i<urlData.length(); i++) urls.add(urlData.getObject(i).getObject("jpg").getString("image_url"));
        return urls.get((int)(Math.random()*urls.size()));
    }

}
