package lollipop;

import java.io.*;
import java.util.HashSet;

import static lollipop.Constant.*;

/**
 * Caches objects temporarily instead of using a database
 */
public class Cache {

    // Title cache in hashset
    public static HashSet<String> titles = new HashSet<>();

    static {
        BufferedReader bf = null;
        try { bf = new BufferedReader(new FileReader(TITLECACHE)); }
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
        BufferedReader bf = new BufferedReader(new FileReader(GIFCACHE));
        StringBuilder file = new StringBuilder();
        String line;
        while((line=bf.readLine()) != null) file.append(line).append("\n");
        file.append(gif).append("\n");
        bf.close();
        FileWriter fw = new FileWriter(GIFCACHE);
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
        BufferedReader bf = new BufferedReader(new FileReader(TITLECACHE));
        StringBuilder file = new StringBuilder();
        String line;
        while((line=bf.readLine()) != null) file.append(line).append("\n");
        file.append(title).append("\n");
        bf.close();
        FileWriter fw = new FileWriter(TITLECACHE);
        fw.write(file.toString());
        fw.close();
    }

}
