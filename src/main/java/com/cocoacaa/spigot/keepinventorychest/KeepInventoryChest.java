package com.cocoacaa.spigot.keepinventorychest;

import com.cocoacaa.spigot.keepinventorychest.configs.MessagesConfig;
import com.cocoacaa.spigot.keepinventorychest.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class KeepInventoryChest extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            MessagesConfig messagesConfig = new MessagesConfig(this);
            messagesConfig.load();
            MessageSender messageSender = new MessageSender(this);
            getServer().getPluginManager().registerEvents(new PlayerListener(this, messagesConfig, messageSender), this);
            getLogger().info("KeepInventoryChest Enabled!");
        } catch (Exception ex) {
            getLogger().severe("Cannot enable plugin, please see below information for details:");
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("KeepInventoryChest Disabled!");
    }
}
