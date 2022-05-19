package lollipop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Cache {

    public static String gifCache = "/home/boole/Documents/bots/discord/lollipop/src/main/java/lollipop/cache/gifs.txt";

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
