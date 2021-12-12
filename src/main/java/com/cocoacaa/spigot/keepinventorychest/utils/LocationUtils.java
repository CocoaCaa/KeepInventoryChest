package com.cocoacaa.spigot.keepinventorychest.utils;

import org.bukkit.Location;
import org.bukkit.Material;

public class LocationUtils {
    public static Location getAvailableAirBlockLocation(Location center, int radius) {
        if (center.getBlock().getType() == Material.AIR) {
            return center.clone();
        }

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    var target = center.clone().add(x, y, z);
                    if (target.getBlock().getType() == Material.AIR) {
                        return target;
                    }
                }
            }
        }
        return null;
    }
}
