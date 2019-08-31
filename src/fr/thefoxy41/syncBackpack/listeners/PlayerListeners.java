package fr.thefoxy41.syncBackpack.listeners;

import fr.thefoxy41.syncBackpack.core.managers.BackpackManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        BackpackManager.loadPlayer(event.getPlayer());
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        BackpackManager.removePlayer(event.getPlayer().getUniqueId());
    }
}
