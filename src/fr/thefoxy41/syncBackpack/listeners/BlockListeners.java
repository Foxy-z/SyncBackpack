package fr.thefoxy41.syncBackpack.listeners;

import fr.thefoxy41.syncBackpack.core.managers.ChestManager;
import fr.thefoxy41.syncBackpack.core.objects.SyncChest;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.CHEST)) return;

        SyncChest chest = ChestManager.getChestAt(event.getBlock().getLocation());
        if (chest != null) {
            ChestManager.removeChest(chest.getName());
        }
    }
}
