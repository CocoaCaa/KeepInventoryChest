package com.cocoacaa.spigot.keepinventorychest.configs;

import org.bukkit.plugin.Plugin;

public class MessagesConfig extends ConfigBase {
    public MessagesConfig (Plugin plugin) {
        super(plugin, "messages.yml");
    }

    public String getCannotCreateChest() {
        return config.getString("cannotCreateChest");
    }

    public String getChestCreated() {
        return config.getString("chestCreated");
    }
}
