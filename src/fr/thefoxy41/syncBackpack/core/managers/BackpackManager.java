package fr.thefoxy41.syncBackpack.core.managers;

import fr.thefoxy41.syncBackpack.SyncBackpack;
import fr.thefoxy41.syncBackpack.core.Configuration;
import fr.thefoxy41.syncBackpack.core.objects.Backpack;
import fr.thefoxy41.syncBackpack.core.objects.Head;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseConnectionException;
import fr.thefoxy41.syncBackpack.database.objects.Query;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BackpackManager {
    private static final Map<UUID, Backpack> BACKPACKS = new HashMap<>();

    private static Map<Integer, Head> fromResultSet(ResultSet results) throws SQLException {
        Map<Integer, Head> heads = new HashMap<>();
        while (results.next()) {
            if (results.getString("texture") == null) continue;

            heads.put(
                    results.getInt("slot"),
                    new Head(results.getString("name"),
                            results.getInt("amount"),
                            results.getString("texture")
                    )
            );
        }
        return heads;
    }

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(SyncBackpack.INSTANCE, () -> {
            try {
                ResultSet results = (new Query()).from(Configuration.BACKPACKS_TABLE).select().join(Configuration.BACKPACKS_ITEMS_TABLE, Configuration.BACKPACKS_TABLE + ".id = backpack_id").where(Configuration.BACKPACKS_TABLE + ".uuid", uuid.toString()).execute();

                if (results.next()) {
                    results.previous();
                    BACKPACKS.put(uuid, new Backpack(player, fromResultSet(results)));
                } else {
                    BACKPACKS.put(uuid, new Backpack(player, new HashMap<>()));
                    savePlayer(uuid);
                }
            } catch (DatabaseConnectionException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void savePlayer(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(SyncBackpack.INSTANCE, () -> {
            try {
                Backpack backpack = BACKPACKS.get(uuid);
                new Query()
                        .from(Configuration.BACKPACKS_TABLE)
                        .insertOrUpdate("uuid", "level")
                        .params(uuid.toString(), String.valueOf(backpack.getLevel()))
                        .execute();

                ResultSet backpackResult = new Query()
                        .from(Configuration.BACKPACKS_TABLE)
                        .select("id")
                        .where("uuid", uuid.toString())
                        .execute();

                if (!backpackResult.next())
                    throw new NullPointerException("Backpack for player " + uuid.toString() + " hasn't been found!");

                int backpackId = backpackResult.getInt("id");
                ResultSet itemsResults = new Query()
                        .from(Configuration.BACKPACKS_ITEMS_TABLE)
                        .select()
                        .where("backpack_id", String.valueOf(backpackId))
                        .updatable()
                        .execute();

                Set<Integer> slots = backpack.getUsedSlots();
                while (itemsResults.next()) {
                    int slot = itemsResults.getInt("slot");
                    if (slots.contains(slot)) {
                        Head head = backpack.getHeadAt(slot);
                        itemsResults.updateString("name", head.getName());
                        itemsResults.updateInt("amount", head.getAmount());
                        itemsResults.updateString("texture", head.getBase64());
                        slots.remove(slot);
                    } else {
                        itemsResults.deleteRow();
                    }
                }
                if (slots.isEmpty()) return;

                for (int i : slots) {
                    Head head = backpack.getHeadAt(i);
                    new Query()
                            .from(Configuration.BACKPACKS_ITEMS_TABLE)
                            .insert("backpack_id", "slot", "name", "amount", "texture")
                            .params(
                                    String.valueOf(backpackId),
                                    String.valueOf(i),
                                    (head.getName() != null) ? head.getName() : "",
                                    String.valueOf(head.getAmount()),
                                    head.getBase64()
                            ).execute();
                }
            } catch (DatabaseConnectionException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void removePlayer(UUID uuid) {
        BACKPACKS.remove(uuid);
    }

    public static Backpack get(UUID uuid) {
        return BACKPACKS.get(uuid);
    }
}
