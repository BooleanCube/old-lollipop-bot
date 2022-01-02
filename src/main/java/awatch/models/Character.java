package awatch.models;

public class Character {

    public String art;
    public String name;
    public int malID;
    public String alternativeNames = "None";
    public String url;
    public String anime = "None";
    public String manga = "None";

    public String toString() {
        return "Charcter [" + malID + ": " + name + "]";
    }

}
