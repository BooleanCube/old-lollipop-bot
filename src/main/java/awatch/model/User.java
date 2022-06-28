package awatch.model;

import awatch.ModelData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class User implements ModelData {

    public String username;
    public String url;
    public String picture;
    public String gender;
    public String birthday;
    public String joined;
    public String location;
    public String about;

    // Updates
    public String updateAnime;
    public String updateManga;

    // Favorites
    public String favoriteAnime;
    public String favoriteManga;
    public String favoriteCharacter;

    // Statistics
    public String animeStats;
    public String mangaStats;

    public String toString() {
        return this.username + " - " + this.url;
    }

    /**
     * Parse data from data object
     * @param data data object to parse from
     */
    @Override
    public void parseData(DataObject data) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
        this.username = data.getString("username", "Unknown username");
        this.url = data.getString("url", "https://myanimelist.net/");
        this.picture = data.getObject("images").getObject("jpg").getString("image_url", "");
        this.gender = data.getString("gender", "Unknown");
        this.birthday = data.getString("birthday", "Unknown");
        if(!this.birthday.equals("Unknown")) this.birthday = OffsetDateTime.parse(this.birthday).format(dateTimeFormatter);
        this.location = data.getString("location", "Unknown");
        this.joined = OffsetDateTime.parse(data.getString("joined")).format(dateTimeFormatter);
        this.about = data.getString("about", "No biography found!");

        DataArray arr = data.getObject("updates").getArray("anime");
        this.updateAnime = "";
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String title = res.getObject("entry").getString("title", "Unknown Anime");
            String url = res.getObject("entry").getString("url", "https://myanimelist.net/");
            String status = res.getString("status", "Watching");
            this.updateAnime += "[" + title + "](" + url + ") (" + status + "), ";
        }
        if(arr.length() == 0) this.updateAnime = "No anime updates found!";
        else this.updateAnime = this.updateAnime.substring(0, this.updateAnime.length()-2);
        arr = data.getObject("updates").getArray("manga");
        this.updateManga = "";
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String title = res.getObject("entry").getString("title", "Unknown Manga");
            String url = res.getObject("entry").getString("url", "https://myanimelist.net/");
            String status = res.getString("status", "Reading");
            this.updateManga += "[" + title + "](" + url + ") (" + status + "), ";
        }
        if(arr.length() == 0) this.updateManga = "No manga updates found!";
        else this.updateManga = this.updateManga.substring(0, this.updateManga.length()-2);

        arr = data.getObject("favorites").getArray("anime");
        this.favoriteAnime = "";
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String title = res.getString("title", "Unknown Anime");
            String url = res.getString("url", "https://myanimelist.net/");
            String type = res.getString("type", "TV");
            this.favoriteAnime += "[" + title + "](" + url + ") (" + type + "), ";
        }
        if(arr.length() == 0) this.favoriteAnime = "No favorite animes found!";
        else this.favoriteAnime = this.favoriteAnime.substring(0, this.favoriteAnime.length()-2);
        arr = data.getObject("favorites").getArray("manga");
        this.favoriteManga = "";
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String title = res.getString("title", "Unknown Manga");
            String url = res.getString("url", "https://myanimelist.net/");
            String type = res.getString("type", "TV");
            this.favoriteManga += "[" + title + "](" + url + ") (" + type + "), ";
        }
        if(arr.length() == 0) this.favoriteManga = "No favorite mangas found!";
        else this.favoriteManga = this.favoriteManga.substring(0, this.favoriteManga.length()-2);
        arr = data.getObject("favorites").getArray("characters");
        this.favoriteCharacter = "";
        for(int i=0; i<arr.length(); i++) {
            DataObject res = arr.getObject(i);
            String name = res.getString("name", "Unknown Character");
            String url = res.getString("url", "https://myanimelist.net/");
            this.favoriteCharacter += "[" + name + "](" + url + "), ";
        }
        if(arr.length() == 0) this.favoriteCharacter = "No favorite characters found!";
        else this.favoriteCharacter = this.favoriteCharacter.substring(0, this.favoriteCharacter.length()-2);

        DataObject animeStats = data.getObject("statistics").getObject("anime");
        double daysAnime = animeStats.getDouble("days_watched", 0.0);
        double meanScoreAnime = animeStats.getDouble("mean_score", 0.0);
        int watchingAnime = animeStats.getInt("watching", 0);
        int completedAnime = animeStats.getInt("completed", 0);
        int holdingAnime = animeStats.getInt("on_hold", 0);
        int droppedAnime = animeStats.getInt("dropped", 0);
        int planningAnime = animeStats.getInt("plan_to_watch", 0);
        int totalAnime = animeStats.getInt("total_entries", 64);
        int rewatched = animeStats.getInt("rewatched", 0);
        int episodesWatched = animeStats.getInt("episodes_watched", 0);
        this.animeStats =
                "Days Watched: " + daysAnime + "\n" +
                "Mean Score: " + meanScoreAnime + "\n" +
                "\n" +
                "Watching: " + watchingAnime + "\n" +
                "Completed: " + completedAnime + "\n" +
                "On Hold: " + holdingAnime + "\n" +
                "Dropped: " + droppedAnime + "\n" +
                "Plan to Watch: " + planningAnime + "\n" +
                "Total: " + totalAnime + "\n" +
                "\n" +
                "Rewatched: " + rewatched + "\n" +
                "Episodes Watched: " + episodesWatched;

        DataObject mangaStats = data.getObject("statistics").getObject("manga");
        double daysManga = mangaStats.getDouble("days_read", 0.0);
        double meanScoreManga = mangaStats.getDouble("mean_score", 0.0);
        int readingManga = mangaStats.getInt("reading", 0);
        int completedManga = mangaStats.getInt("completed", 0);
        int holdingManga = mangaStats.getInt("on_hold", 0);
        int droppedManga = mangaStats.getInt("dropped", 0);
        int planningManga = mangaStats.getInt("plan_to_read", 0);
        int totalManga = mangaStats.getInt("total_entries", 64);
        int reread = mangaStats.getInt("reread", 0);
        int chaptersRead = mangaStats.getInt("chapters_read", 0);
        int volumesRead = mangaStats.getInt("volumes_read", 0);
        this.mangaStats =
                "Days Read: " + daysManga + "\n" +
                "Mean Score: " + meanScoreManga + "\n" +
                "\n" +
                "Reading: " + readingManga + "\n" +
                "Completed: " + completedManga + "\n" +
                "On Hold: " + holdingManga + "\n" +
                "Dropped: " + droppedManga + "\n" +
                "Plan to Read: " + planningManga + "\n" +
                "Total: " + totalManga + "\n" +
                "\n" +
                "Reread: " + reread + "\n" +
                "Chapters Read: " + chaptersRead + "\n" +
                "Volumes Read: " + volumesRead;
    }

    /**
     * parse data from a data array
     * @param data data array to parse from
     */
    @Override
    public void parseData(DataArray data) {

    }

    /**
     * Represent the user in an embed
     * @return EmbedBuilder
     */
    @Override
    public EmbedBuilder toEmbed() {
        return new EmbedBuilder()
                .setAuthor(this.username, this.url, this.picture)
                .setTitle("User Profile")
                .setDescription(
                                "> Joined [MAL](https://myanimelist.net/) on: " +
                                this.joined + "\n" +
                                "> Birthday: " +
                                this.birthday + "\n" +
                                "> Gender: " +
                                this.gender + "\n" +
                                "> Location: " +
                                this.location + "\n" +
                                "\n```\nAbout Me:\n" +
                                this.about +
                                "\n``` "
                )
                .addField("Anime Updates", this.updateAnime, false)
                .addField("Manga Updates", this.updateManga, false)
                .addField("Favorite Animes", this.favoriteAnime, false)
                .addField("Favorite Mangas", this.favoriteManga, false)
                .addField("Favorite Characters", this.favoriteCharacter, false)
                .addField("Anime Statistics", this.animeStats, true)
                .addField("Manga Statistics", this.mangaStats, true);
    }

}
