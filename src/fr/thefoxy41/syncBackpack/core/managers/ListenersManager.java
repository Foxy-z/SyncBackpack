package fr.thefoxy41.syncBackpack.core.managers;

import fr.thefoxy41.syncBackpack.commands.CmdBackpack;
import fr.thefoxy41.syncBackpack.listeners.BlockListeners;
import fr.thefoxy41.syncBackpack.listeners.ChestListeners;
import fr.thefoxy41.syncBackpack.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenersManager {

    public static void registerEvents(Plugin plugin) {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BlockListeners(), plugin);
        manager.registerEvents(new ChestListeners(), plugin);
        manager.registerEvents(new PlayerListeners(), plugin);
    }

    public static void registerCommands(JavaPlugin plugin) {
        plugin.getCommand("backpack").setExecutor(new CmdBackpack());
    }
}
