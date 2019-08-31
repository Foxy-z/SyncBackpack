package fr.thefoxy41.syncBackpack.core.managers;

import fr.thefoxy41.syncBackpack.SyncBackpack;
import fr.thefoxy41.syncBackpack.core.Configuration;
import fr.thefoxy41.syncBackpack.core.Messages;
import fr.thefoxy41.syncBackpack.core.objects.SimpleLocation;
import fr.thefoxy41.syncBackpack.core.objects.SyncChest;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseConnectionException;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseQueryException;
import fr.thefoxy41.syncBackpack.database.objects.Query;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestManager {
    private static final Map<String, SyncChest> CHESTS = new HashMap<>();
    private static final Map<UUID, String> CHEST_CREATION = new HashMap<>();

    public static void loadChests(Plugin plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {

                ResultSet results = new Query()
                        .from(Configuration.CHESTS_TABLE)
                        .select()
                        .where("server_id", Configuration.SERVER_NAME)
                        .execute();

                while (results.next()) {
                    CHESTS.put(
                            results.getString("name"),
                            new SyncChest(
                                    results.getString("name"),
                                    new SimpleLocation(
                                            Bukkit.getWorld(results.getString("world")).getUID(),
                                            results.getInt("x"),
                                            results.getInt("y"),
                                            results.getInt("z")
                                    )
                            )
                    );

                }
            } catch (DatabaseConnectionException | java.sql.SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static boolean isSyncChest(Location location) {
        return CHESTS.values().stream().anyMatch(chest -> chest.getLocation().equals(location));
    }

    public static void setCreatingChest(Player player, String name) {
        UUID uuid = player.getUniqueId();
        CHEST_CREATION.put(uuid, name);

        Bukkit.getScheduler().runTaskLater(SyncBackpack.INSTANCE, () -> {
            if (CHEST_CREATION.containsKey(uuid)) {
                CHEST_CREATION.remove(uuid);
                if (player.isOnline()) {
                    player.sendMessage(Messages.PREFIX + "La création du coffre a expiré.");
                }
            }
        }, 300L);
    }

    public static void createChest(Player player, Location location) {
        UUID uuid = player.getUniqueId();
        String name = CHEST_CREATION.get(uuid);
        CHEST_CREATION.remove(uuid);
        if (CHESTS.containsKey(name)) {
            player.sendMessage(Messages.ERROR + "Un coffre du même nom a été ajouté entre temps...");
            return;
        }

        SimpleLocation sLoc = new SimpleLocation(location);
        SyncChest chest = new SyncChest(name, sLoc);
        CHESTS.put(name, chest);
        saveChest(chest);

        player.sendMessage(Messages.PREFIX + "Le coffre §a" + name + "§7 a été ajouté à l'emplacement x:§a" + sLoc
                .getX() + "§7, y:§a" + sLoc
                .getY() + "§7, z:§a" + sLoc
                .getZ() + "§7.");
    }

    private static void saveChest(SyncChest syncChest) {
        Bukkit.getScheduler().runTaskAsynchronously(SyncBackpack.INSTANCE, () -> {
            try {
                SimpleLocation location = syncChest.getLocation();
                (new Query())
                        .from(Configuration.CHESTS_TABLE)
                        .insertOrUpdate("server_id", "name", "world", "x", "y", "z")
                        .params(
                                Configuration.SERVER_NAME,
                                syncChest.getName(),
                                location.getWorldName(),
                                String.valueOf(location.getX()),
                                String.valueOf(location.getY()),
                                String.valueOf(location.getZ())
                        ).execute();
            } catch (DatabaseConnectionException | DatabaseQueryException e) {
                e.printStackTrace();
            }
        });
    }

    public static void removeChest(String chest) {
        CHESTS.remove(chest);
        Bukkit.getScheduler().runTaskAsynchronously(SyncBackpack.INSTANCE, () -> {
            try {
                (new Query())
                        .from(Configuration.CHESTS_TABLE)
                        .delete()
                        .where("name", chest)
                        .where("server_id", Configuration.SERVER_NAME)
                        .execute();
            } catch (DatabaseConnectionException | DatabaseQueryException e) {
                e.printStackTrace();
            }
        });
    }

    public static SyncChest getChestAt(Location location) {
        return CHESTS.values().stream().filter(chest -> chest.getLocation().equals(location)).findFirst().orElse(null);
    }

    public static boolean chestExists(String name) {
        return (getNameExact(name) != null);
    }

    public static String getNameExact(String chest) {
        return CHESTS.keySet().stream().filter(key -> key.equalsIgnoreCase(chest)).findFirst().orElse(null);
    }

    public static boolean isCreatingChest(UUID uuid) {
        return CHEST_CREATION.containsKey(uuid);
    }
}
