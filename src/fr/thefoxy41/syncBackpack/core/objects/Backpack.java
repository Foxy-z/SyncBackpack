package fr.thefoxy41.syncBackpack.core.objects;

import fr.thefoxy41.syncBackpack.core.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Backpack implements InventoryHolder {
    private Player player;

    public Backpack(Player player, Map<Integer, Head> heads) {
        this.player = player;
        this.heads = heads;
    }

    private Map<Integer, Head> heads;

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setHeads(Map<Integer, Head> heads) {
        this.heads = heads;
    }

    public int getLevel() {
        int level = 1;
        for (int i = 2; i < 7; i++) {
            if (!this.player.hasPermission("chest.level." + i)) break;
            level = i;
        }
        return level;
    }

    private int getSlots() {
        return getLevel() * 9;
    }

    public Head getHeadAt(int slot) {
        return this.heads.get(slot);
    }

    public Set<Integer> getUsedSlots() {
        return new HashSet<>(this.heads.keySet());
    }

    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, getSlots(), Messages.PREFIX + this.player.getName());
        for (int slot : this.heads.keySet()) {
            if (slot >= inventory.getSize() -1) break;
            Head head = this.heads.get(slot);
            inventory.setItem(slot, head.getItem());
        }

        return inventory;
    }

    public void update(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                this.heads.remove(i);
            } else {
                ItemStack item = inventory.getItem(i);
                Head head = new Head(item);
                this.heads.put(i, head);
            }
        }
    }
}
