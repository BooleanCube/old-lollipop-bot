package awatch;

import java.util.List;

public class Anime {

    public int malID;
    public String rating;
    public String title;
    public String art;
    public String url;
    public int episodeCount;
    public int score;
    public String status;
    public String summary;
    public String author;
    public List<String> tags;
    public List<String> episodes;

    public String toString() {
        return "Anime [title=" + title + ", art=" + art + ", url=" + url + ", episodeCount=" + episodeCount + ", score=" + score
                + ", status=" + status + ", summary=" + summary + ", author=" + author + ", tags=" + tags + ", episodes=" + episodes + "]";
    }

}
