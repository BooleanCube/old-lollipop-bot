package lollipop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Caches objects temporarily instead of using a database
 */
public class Cache {

    // GIF file cache directory
    public static String gifCache = "/home/boole/Documents/bots/discord/lollipop-bot/src/main/java/lollipop/cache/gifs.txt";

    /**
     * Add GIF to cache
     * (done to keep a cache of GIFs in case we get banned from using the GIF API again..)
     * @param gif GIF url
     * @throws IOException for BufferedReader
     */
    public static void addGifToCache(String gif) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(gifCache));
        StringBuilder file = new StringBuilder();
        String line;
        while((line=bf.readLine()) != null) file.append(line).append("\n");
        file.append(gif).append("\n");
        bf.close();
        FileWriter fw = new FileWriter(gifCache);
        fw.write(file.toString());
        fw.close();
    }

}
