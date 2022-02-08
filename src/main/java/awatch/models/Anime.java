package awatch.models;

import java.util.List;

public class Anime {

    public int malID;
    public String rating = "Unknown";
    public String title;
    public String art;
    public String url;
    public int episodeCount;
    public double score;
    public String status = "Unkown";
    public String summary;
    public String author;
    public List<String> tags;
    public List<String> episodes;
    public int rank;
    public String trailer;
    public String type = "None";
    public int popularity = 0;

    public String toString() {
        return "Anime [title=" + title + ", art=" + art + ", url=" + url + ", episodeCount=" + episodeCount + ", score=" + score
                + ", status=" + status + ", summary=" + summary + ", author=" + author + ", tags=" + tags + ", episodes=" + episodes + ", popularity=" + popularity + "]";
    }

}
