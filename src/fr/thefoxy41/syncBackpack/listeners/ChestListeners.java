package fr.thefoxy41.syncBackpack.listeners;

import fr.thefoxy41.syncBackpack.core.Messages;
import fr.thefoxy41.syncBackpack.core.managers.BackpackManager;
import fr.thefoxy41.syncBackpack.core.managers.ChestManager;
import fr.thefoxy41.syncBackpack.core.objects.Backpack;
import fr.thefoxy41.syncBackpack.utils.ReflectionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


public class ChestListeners implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void on(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.CHEST) return;

        Player player = event.getPlayer();
        if (ChestManager.isCreatingChest(player.getUniqueId())) {
            event.setCancelled(true);
            ChestManager.createChest(player, block.getLocation());
            return;
        }

        if (!player.hasPermission("syncbackpack.open")) {
            player.sendMessage(Messages.PREFIX + "Tu n'as pas la permission d'ouvrir ce coffre.");
            return;
        }

        if (!ChestManager.isSyncChest(block.getLocation())) return;

        Backpack backpack = BackpackManager.get(player.getUniqueId());
        player.openInventory(backpack.getInventory());
        event.setCancelled(true);
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof Backpack)) return;

        ItemStack item = event.getCurrentItem();
        if (item.getType() != Material.valueOf(ReflectionUtils.getSkullEnumConstant())
                && item.getDurability() != 3
                && item.getDurability() != -1) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(Messages.PREFIX + "Vous ne pouvez pas placer autre chose qu'une tête de joueur.");
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof Backpack)) return;

        UUID uuid = event.getPlayer().getUniqueId();

        BackpackManager.get(uuid).update(event.getView().getTopInventory());
        BackpackManager.savePlayer(uuid);
    }
}
