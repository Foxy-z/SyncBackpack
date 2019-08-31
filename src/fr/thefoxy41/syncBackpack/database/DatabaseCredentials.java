package fr.thefoxy41.syncBackpack.database;

import fr.thefoxy41.syncBackpack.core.managers.FileManager;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

class DatabaseCredentials {
    private final String host;
    private final String pass;
    private final int port;
    private final String user;
    private final String dataBaseName;

    DatabaseCredentials(File file) throws DatabaseConfigurationException {
        YamlConfiguration configuration = FileManager.getConfiguration(file);
        if (configuration == null) {
            throw new DatabaseConfigurationException("Configuration file is not valid");
        }

        if (!configuration.contains("host") ||
                !configuration.contains("port") ||
                !configuration.contains("user") ||
                !configuration.contains("password") ||
                !configuration.contains("name")) {
            throw new DatabaseConfigurationException("Parameter forgotten in the configuration file");
        }

        this.host = configuration.getString("host");
        this.port = configuration.getInt("port");
        this.user = configuration.getString("user");
        this.pass = configuration.getString("password");
        this.dataBaseName = configuration.getString("name");
    }

    String toURI() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dataBaseName + "?useSSL=false";
    }

    String getUser() {
        return this.user;
    }

    String getPass() {
        return this.pass;
    }
}
