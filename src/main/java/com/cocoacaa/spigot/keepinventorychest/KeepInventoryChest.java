package com.cocoacaa.spigot.keepinventorychest;

import com.cocoacaa.spigot.keepinventorychest.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class KeepInventoryChest extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("KeepInventoryChest Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("KeepInventoryChest Disabled!");
    }
}
