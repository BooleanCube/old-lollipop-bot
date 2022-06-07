package discorddb;


import javax.naming.LimitExceededException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Database Manager for Discord DB library
 */
public class DatabaseManager {

    private static final HashMap<String, DatabaseObject> databases;
    private static final String directory = Paths.get("").toAbsolutePath() + "/src/main/java/discorddb/files/";

    /**
     * Static block to init all the current databases and caches
     */
    static {
        databases = new HashMap<>();
        File filesFolder = new File(directory);
        if(!filesFolder.exists()) filesFolder.mkdir();
        File[] files = filesFolder.listFiles();
        if(files != null) {
            for(File dbFile : files) {
                String dbName = dbFile.getName().replace(".json","");
                DatabaseObject db = new DatabaseObject(dbName, dbFile);
                databases.put(dbName, db);
            }
        }
    }

    /**
     * Creates a database file and adds it to the cache
     * @param dbName
     * @return boolean indicating success or failure
     * @throws LimitExceededException 10 databases at max
     * @throws FileAlreadyExistsException no duplicate databases can be created
     * @throws FileNotFoundException could not find directory
     */
    public static boolean createDatabase(String dbName) throws LimitExceededException, FileAlreadyExistsException, FileNotFoundException {
        if(databases.size() >= 10)
            throw new LimitExceededException("Cannot create more than 10 databases!");
        if(databases.containsKey(dbName))
            return true;

        try {
            File dbFile = new File(directory + dbName + ".json");
            if(!dbFile.exists()) {
                if(dbFile.createNewFile()) {
                    FileWriter fr = new FileWriter(dbFile);
                    fr.write("{\"data\":{}}");
                    fr.close();
                    databases.put(dbName, new DatabaseObject(dbName, dbFile));
                    System.out.println("Successfully created the database file!");
                }
            }
            else
                throw new FileAlreadyExistsException("This database file has already been created!");
        } catch(IOException ignored) {
            throw new FileNotFoundException("There is no such directory/file!");
        }

        return true;
    }

    /**
     * Returns the database object based on the database name
     * @param dbName database name
     * @return DatabaseObject representing a database
     */
    public static DatabaseObject getDatabase(String dbName) {
        if(!databases.containsKey(dbName)) return null;
        return databases.get(dbName);
    }

}
