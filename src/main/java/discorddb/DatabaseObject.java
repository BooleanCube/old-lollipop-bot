package discorddb;

import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.*;
import java.util.*;

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
        try { initCache(); } catch (IOException ignored) {}
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
     * @return {@link String} with database name
     */
    public String getName() {
        return dbName;
    }

    /**
     * Get database file
     * @return {@link File} with all of the data
     */
    public File getFile() {
        return dbFile;
    }

    /**
     * Get all of the keys stored inside the database
     * @return {@link Set<String>} string set of keys
     */
    public Set<String> getKeys() {
        return cache.keySet();
    }

    /**
     * Get all of the values stored inside the database
     * @return {@link Collection<String>} string collection of values
     */
    public Collection<String> getValues() {
        return cache.values();
    }

    /**
     * Get value given key from the cache
     * @param key String key
     * @return String value corresponding to key
     */
    public String getValue(String key) {
        if(!cache.containsKey(key)) return null;
        return cache.get(key);
    }

    /**
     * Get value given key as integer from the cache
     * @param key String key
     * @return Integer value corresponding to key
     */
    public Integer getValueInt(String key) {
        if(!cache.containsKey(key)) return null;
        return Integer.parseInt(cache.get(key));
    }

    /**
     * Get value given key as a long from the cache
     * @param key String key
     * @return Long value corresponding to key
     */
    public Long getValueLong(String key) {
        if(!cache.containsKey(key)) return null;
        return Long.parseLong(cache.get(key));
    }

    /**
     * Update value at key, Add if key does not exist
     * (both database and cache)
     * @param key key String to new value
     * @param value new value String
     * @throws IOException for FileWriter
     */
    public void updateValue(String key, String value) throws IOException {
        if(!cache.containsKey(key)) {
            addKey(key, value);
            return;
        }
        cache.replace(key, value);
        updateToDb(key, value);
    }

    /**
     * Add key to value relationship
     * (both database and cache)
     * @param key key String of value
     * @param value value String corresponding to key
     * @throws IOException for FileWriter
     */
    public void addKey(String key, String value) throws IOException {
        cache.put(key, value);
        updateToDb(key, value);
    }

    /**
     * Remove key
     * both database and cache
     * @param key key String to be removed
     * @throws IOException for FileWriter
     */
    public void removeKey(String key) throws IOException {
        cache.remove(key);
        removeFromDb(key);
    }

    /**
     * Update part of the database
     * @param key key String to update
     * @param value new value String
     * @throws IOException file does not exist
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
     * @param key key String to remove
     * @throws IOException file does not exist
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
