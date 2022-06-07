package lollipop;


import discorddb.DatabaseManager;
import discorddb.DatabaseObject;

import javax.naming.LimitExceededException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;


public class Database {

    private static DatabaseObject currency;

    public static void setupDatabases() {
        try {
            DatabaseManager.createDatabase("currency");
            currency = DatabaseManager.getDatabase("currency");
        } catch (LimitExceededException | FileAlreadyExistsException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
