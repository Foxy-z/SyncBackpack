package fr.thefoxy41.syncBackpack.core.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class SimpleLocation {
    private final UUID worldUuid;
    private final int x, y, z;

    public SimpleLocation(Location location) {
        this.worldUuid = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public SimpleLocation(UUID worldUuid, int x, int y, int z) {
        this.worldUuid = worldUuid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Location location) {
        return location.getWorld().getUID().equals(this.worldUuid)
                && location.getBlockX() == this.x
                && location.getBlockY() == this.y
                && location.getBlockZ() == this.z;
    }

    public String getWorldName() {
        return Bukkit.getWorld(this.worldUuid).getName();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}
