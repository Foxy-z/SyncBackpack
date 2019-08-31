package fr.thefoxy41.syncBackpack.database.exceptions;

import org.bukkit.Bukkit;

import java.sql.SQLException;

public class DatabaseQueryException extends SQLException {

    public DatabaseQueryException(String message) {
        super(message);
    }

    public DatabaseQueryException(String message, String query) {
        super(message);
        Bukkit.getConsoleSender().sendMessage("Query: BEGIN|" + query + "|END");
    }
}
