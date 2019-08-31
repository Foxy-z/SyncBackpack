package fr.thefoxy41.syncBackpack.database;

import fr.thefoxy41.syncBackpack.core.Configuration;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.File;


public class DatabaseManager {
    private static DatabaseAccess dataBase;

    public static DatabaseAccess getDataBase() {
        return dataBase;
    }

    public static void initDataBaseConnection(Plugin plugin) throws DatabaseConfigurationException {
        dataBase = new DatabaseAccess(new DatabaseCredentials(new File(Configuration.FILE_PATH, Configuration.FILE_NAME)));
        dataBase.initPool(plugin);
    }

    public static void closeDataBaseConnection() {
        if (dataBase != null) dataBase.closePool();
    }
}
