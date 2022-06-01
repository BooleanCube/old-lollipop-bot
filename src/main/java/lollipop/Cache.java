package lollipop;

import java.io.*;
import java.util.HashSet;

/**
 * Caches objects temporarily instead of using a database
 */
public class Cache {

    // Title cache in hashset
    public static HashSet<String> titles = new HashSet<>();

    // Base directory
    public static String absPath = System.getProperty("user.dir");

    // GIF file cache directory
    public static String gifCache = "/src/main/java/cache/gifs.txt";

    // Anime Title file cache directory
    public static String titleCache = "/src/main/java/cache/titles.txt";

    static {
        BufferedReader bf = null;
        try { bf = new BufferedReader(new FileReader(absPath + titleCache)); }
        catch (FileNotFoundException e) { /* continue */}
        String input;
        while (true) {
            try {
                if((input=bf.readLine()) == null) break;
                titles.add(input);
            } catch (IOException e) { /* continue */ }
        }
    }

    /**
     * Add GIF to cache
     * (done to keep a cache of GIFs in case we get banned from using the GIF API again..)
     * @param gif GIF url
     * @throws IOException for BufferedReader
     */
    public static void addGifToCache(String gif) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(absPath + gifCache));
        StringBuilder file = new StringBuilder();
        String line;
        while((line=bf.readLine()) != null) file.append(line).append("\n");
        file.append(gif).append("\n");
        bf.close();
        FileWriter fw = new FileWriter(absPath + gifCache);
        fw.write(file.toString());
        fw.close();
    }

    /**
     * Add Title to Cache
     * @param title anime title names
     * @throws IOException for BufferedReader
     */
    public static void addTitleToCache(String title) throws IOException {
        if(titles.contains(title)) return;
        titles.add(title);
        BufferedReader bf = new BufferedReader(new FileReader(absPath + titleCache));
        StringBuilder file = new StringBuilder();
        String line;
        while((line=bf.readLine()) != null) file.append(line).append("\n");
        file.append(title).append("\n");
        bf.close();
        FileWriter fw = new FileWriter(absPath + titleCache);
        fw.write(file.toString());
        fw.close();
    }

}
