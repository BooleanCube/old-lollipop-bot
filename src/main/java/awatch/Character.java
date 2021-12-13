package awatch;

public class Character {

    public String art;
    public String name;
    public String manga = "None";
    public int malID;
    public String alternativeNames = "None";
    public String url;
    public String anime = "None";

    public String toString() {
        return "Charcter [" + malID + ": " + name + "]";
    }

}
