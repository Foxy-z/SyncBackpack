package fr.thefoxy41.syncBackpack;

import fr.thefoxy41.syncBackpack.core.Configuration;
import fr.thefoxy41.syncBackpack.core.managers.BackpackManager;
import fr.thefoxy41.syncBackpack.core.managers.ChestManager;
import fr.thefoxy41.syncBackpack.core.managers.ListenersManager;
import fr.thefoxy41.syncBackpack.database.DatabaseManager;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseConfigurationException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public class SyncBackpack extends JavaPlugin {
    public static SyncBackpack INSTANCE;

    public void onEnable() {
        INSTANCE = this;
        log("Plugin is enabling...");

        try {
            Configuration.init(this);
        } catch (InvalidConfigurationException e) {
            log("An error occurred while reading configuration file. The plugin will shut down...");
            e.printStackTrace();
            onDisable();
        }

        try {
            DatabaseManager.initDataBaseConnection(this);
        } catch (DatabaseConfigurationException e) {
            log("An error occurred when connecting to the database. The plugin will shut down...");
            e.printStackTrace();
            onDisable();
        }

        ListenersManager.registerEvents(this);
        ListenersManager.registerCommands(this);

        ChestManager.loadChests(this);

        Bukkit.getScheduler().runTaskAsynchronously(this, () ->
                Bukkit.getOnlinePlayers().forEach(BackpackManager::loadPlayer)
        );
        log("Plugin has been enabled!");
    }

    public void onDisable() {
        log("Plugin is disabling...");

        DatabaseManager.closeDataBaseConnection();

        log("Plugin has been disabled!");
    }

    private static void log(String message) {
        Bukkit.getConsoleSender().sendMessage("[" + INSTANCE.getName() + "] " + message);
    }
}
