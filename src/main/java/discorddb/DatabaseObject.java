package discorddb;

import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.*;
import java.util.HashMap;

/**
 * Model for Database Object
 */
public class DatabaseObject {

    private final String dbName;
    private final File dbFile;
    private final HashMap<String, String> cache;

    /**
     * Constructor to initialize the object data
     * @param dbName database name
     * @param dbFile database file
     */
    protected DatabaseObject(String dbName, File dbFile) {
        this.dbName = dbName;
        this.dbFile = dbFile;
        this.cache = new HashMap<>();
        try { initCache(); } catch (IOException e) {}
    }

    /**
     * Initialize the cache with the data from the database file
     * @throws IOException for BufferedReader
     */
    private void initCache() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(dbFile));
        DataObject data = DataObject.fromJson(bf.readLine()).getObject("data");
        for(String key : data.keys())
            cache.put(key, data.getString(key));
    }

    /**
     * Get database name
     * @return Database name
     */
    public String getName() {
        return dbName;
    }

    /**
     * Get value given key
     * from cache
     * @param key String key
     * @return value corresponding to key
     */
    public String getValue(String key) {
        if(!cache.containsKey(key)) return null;
        return cache.get(key);
    }

    /**
     * Get value given key as integer
     * from cache
     * @param key String key
     * @return int value corresponding to key
     */
    public Integer getValueInt(String key) {
        if(!cache.containsKey(key)) return null;
        return Integer.parseInt(cache.get(key));
    }

    /**
     * Get value given key as long
     * from cache
     * @param key String key
     * @return long value corresponding to key
     */
    public Long getValueLong(String key) {
        if(!cache.containsKey(key)) return null;
        return Long.parseLong(cache.get(key));
    }

    /**
     * Update value at key, Add if key does not exist
     * both database and cache
     * @param key key to new value
     * @param value new value
     */
    public void updateValue(String key, String value) {
        if(!cache.containsKey(key)) {
            addKey(key, value);
            return;
        }
        cache.replace(key, value);
        try {
            updateToDb(key, value);
        } catch (IOException e) {}
    }

    /**
     * Add key to value relationship
     * both database and cache
     * @param key key of value
     * @param value value corresponding to key
     */
    public void addKey(String key, String value) {
        cache.put(key, value);
        try {
            updateToDb(key, value);
        } catch (IOException e) {}
    }

    /**
     * Remove key
     * both database and cache
     * @param key key to be removed
     */
    public void removeKey(String key) {
        cache.remove(key);
        try {
            removeFromDb(key);
        } catch (IOException e) {}
    }

    /**
     * Update part of the database
     * @param key key to update
     * @param value new value
     * @throws IOException for BufferedReader
     */
    private void updateToDb(String key, String value) throws IOException {
        BufferedReader bf = new BufferedReader( new FileReader(dbFile));
        DataObject result = DataObject.fromJson(bf.readLine());
        DataObject data = result.getObject("data");
        data.put(key,value);
        result.put("data", data);
        FileWriter fw = new FileWriter(dbFile);
        fw.write(result.toString());
        fw.close();
    }

    /**
     * Remove part of the database
     * @param key key to remove
     * @throws IOException for BufferedReader
     */
    private void removeFromDb(String key) throws IOException {
        BufferedReader bf = new BufferedReader( new FileReader(dbFile));
        DataObject result = DataObject.fromJson(bf.readLine());
        DataObject data = result.getObject("data");
        data.remove(key);
        result.put("data", data);
        FileWriter fw = new FileWriter(dbFile);
        fw.write(result.toString());
        fw.close();
    }

}
