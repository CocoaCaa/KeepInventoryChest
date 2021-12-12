package com.cocoacaa.spigot.keepinventorychest;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MessageSender {
    private final static char ALTERNATE_COLOR_CODE_PREFIX = '&';
    private final Plugin plugin;

    public MessageSender(Plugin plugin) {
        this.plugin = plugin;
    }

    public void send(String message, Player player) {
        player.sendMessage(format(message));
    }

    public String format(String message) {
        return String.format("%s %s", getPrefix(), ChatColor.translateAlternateColorCodes(ALTERNATE_COLOR_CODE_PREFIX, message));
    }

    public String getPrefix() {
        return String.format("%s[%s%s%s]%s", ChatColor.GREEN, ChatColor.WHITE, plugin.getName(), ChatColor.GREEN, ChatColor.RESET);
    }
}
