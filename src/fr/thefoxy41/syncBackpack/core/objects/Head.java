package fr.thefoxy41.syncBackpack.core.objects;

import fr.thefoxy41.syncBackpack.utils.HeadUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Head {
    private String name;
    private int amount;
    private String base64;
    private ItemStack item;

    public Head(ItemStack item) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        this.name = meta.hasDisplayName() ? meta.getDisplayName() : null;
        this.amount = item.getAmount();
        this.base64 = HeadUtils.extractHeadTexture(meta);
        this.item = item;
    }

    public Head(String name, int amount, String base64) {
        this.name = name.isEmpty() ? null : name;
        this.amount = amount;
        this.base64 = base64;
        this.item = HeadUtils.getSkull(name, amount, base64);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBase64() {
        return this.base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
