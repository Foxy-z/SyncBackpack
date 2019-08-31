package fr.thefoxy41.syncBackpack.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;


public class HeadUtils {

    public static ItemStack getSkull(String name, int amount, String texture) {
        ItemStack head = new ItemStack(Material.valueOf(ReflectionUtils.getSkullEnumConstant()), amount);
        head.setDurability((short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(name);

        try {
            applyTexture(meta, texture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        head.setItemMeta(meta);
        return head;
    }

    public static String extractHeadTexture(Player player) {
        Property skin = getSkin(player);
        if (skin != null) {
            return skin.getValue();
        }
        return null;
    }

    public static String extractHeadTexture(SkullMeta meta) {
        GameProfile profile = (GameProfile) ReflectionUtils.getDeclaredField(meta, "profile");
        if (profile != null) {
            return String.valueOf(((Property) profile.getProperties().get("textures").toArray()[0]).getValue());
        }
        return null;
    }

    public static UUID extractHeadUuid(SkullMeta meta) {
        GameProfile profile = (GameProfile) ReflectionUtils.getDeclaredField(meta, "profile");
        if (profile != null) {
            return profile.getId();
        }
        return null;
    }

    private static Property getSkin(Player player) {
        Class<?> craftPlayer = ReflectionUtils.getClass("CraftPlayer");
        if (craftPlayer == null) return null;
        craftPlayer.cast(player);
        GameProfile gameProfile = (GameProfile) ReflectionUtils.callMethod(player, "getProfile", new Object[0]);
        if (gameProfile != null) {
            return (Property) gameProfile.getProperties().get("textures").toArray()[0];
        }
        return null;
    }

    private static void applyTexture(SkullMeta meta, String texture) throws NoSuchFieldException, IllegalAccessException {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        Property skin = new Property("textures", texture);
        profile.getProperties().put("textures", skin);

        Field profileField = meta.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        profileField.set(meta, profile);
    }
}
