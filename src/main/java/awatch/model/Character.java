package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Character Model
 */
public class Character implements ModelData {

    // search
    public String art;
    public String name;
    public String nameKanji;
    public int malID;
    public String nickNames = "None";
    public int favorites;
    public String url;
    public String about;

    // character list
    public String role = "Unknown";

    // components
    public String animes;
    public String mangas;
    public String voiceActors;

    /**
     * Returns a string with all the data
     * @return string
     */
    public String toString() {
        return "Charcter [" + malID + ": " + name + "]";
    }

    /**
     * Parses all the data
     * @param data data object for data
     */
    @Override
    public void parseData(DataObject data) {
        this.art = data.getObject("images").getObject("jpg").getString("image_url");
        this.name = data.getString("name", "Unknown");
        this.nameKanji = data.getString("name_kanji", "Unknown");
        this.favorites = data.getInt("favorites", -1);
        this.about = data.getString("about", "No description found!");
        this.malID = data.getInt("mal_id", -1);
        if(data.getArray("nicknames").length() > 0)
            this.nickNames = data.getArray("nicknames")
                    .toList()
                    .stream()
                    .map(a -> Objects.toString(a, null))
                    .collect(Collectors.joining(", "));
        else this.nickNames = "None found";
        this.url = data.getString("url");
    }

    /**
     * Parse the data for the componenets
     * @param data data object to parse from
     */
    public void parseComponents(DataObject data) {
        this.animes = ""; this.mangas = ""; this.voiceActors = "";
        DataArray arr = data.getArray("anime");
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String role = res.getString("role", "Unknown");
            String anime = res.getObject("anime").getString("title", "Unknown");
            String url = res.getObject("anime").getString("url", "https://myanimelist.net/");
            this.animes += "> [" + anime + "](" + url + ")\n> Role: " + role + " character\n\n";
        }
        if(arr.length() == 0) this.animes = "No animes found with this character!";
        arr = data.getArray("manga");
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String role = res.getString("role", "Unknown");
            String manga = res.getObject("manga").getString("title", "Unknown");
            String url = res.getObject("manga").getString("url", "https://myanimelist.net/");
            this.mangas += "> [" + manga + "](" + url + ")\n> Role: " + role + " character\n\n";
        }
        if(arr.length() == 0) this.mangas = "No mangas found with this character!";
        arr = data.getArray("voices");
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String language = res.getString("language", "Unknown");
            String person = res.getObject("person").getString("name", "Unknown");
            String url = res.getObject("person").getString("url", "https://myanimelist.net/");
            this.voiceActors += "> [" + person + "](" + url + ")\n> Language: " + language + "\n\n";
        }
        if(arr.length() == 0) this.voiceActors = "No voice actors found for this character!";
    }

    /**
     * Special parse data method for character list
     * @param data data object to parse
     */
    public void parseDataList(DataObject data) {
        DataObject res = data.getObject("character");
        this.name = res.getString("name", "Unknown");
        this.url = res.getString("url", "https://myanimelist.net/");
        this.role = data.getString("role", "Unknown");
    }

    /**
     * Parses all the data
     * @param data parse data from the data array
     */
    @Override
    public void parseData(DataArray data) {
        // empty
    }

    /**
     * Compresses all the data into an EmbedBuilder
     * @return embedbuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setAuthor("ID: " + this.malID, this.url)
                .setTitle(this.name + " (" + this.nameKanji + ")")
                .setDescription(this.about)
                .addField("Nicknames", this.nickNames, false)
                .setImage(this.art);
    }

}
