package com.cocoacaa.spigot.keepinventorychest.listeners;

import com.cocoacaa.spigot.keepinventorychest.KeepInventoryChest;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PlayerListener implements Listener {
    private final KeepInventoryChest plugin;
    private final NamespacedKey namespacedKey;

    public PlayerListener(KeepInventoryChest plugin) {
        this.plugin = plugin;
        this.namespacedKey = new NamespacedKey(plugin, "keep-inventory-chest-drops");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var originalDrops = event.getDrops();
        var drops = originalDrops
                .stream()
                .map(ItemStack::clone)
                .toList();
        for (var drop : originalDrops) {
            drop.setAmount(0);
        }

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            var location = event.getEntity().getLocation();
            var block = location.getBlock();
            block.setType(Material.CHEST);
            var chest = (Chest) block.getState();
            var inventory = chest.getBlockInventory();

            chest.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BYTE, (byte) 1);
            chest.update();

            var overflowItems = inventory
                    .addItem(drops.toArray(new ItemStack[0]))
                    .values()
                    .stream()
                    .map(ItemStack::clone)
                    .toList();

            for (var overflowItem : overflowItems) {
                Objects.requireNonNull(location.getWorld()).dropItem(location, overflowItem);
            }

            var chestBlockData = (org.bukkit.block.data.type.Chest) chest.getBlockData();
            var nextFacingLocation = location.clone().add(chestBlockData.getFacing().getDirection());
            var itemFrame = Objects.requireNonNull(location.getWorld())
                    .spawn(nextFacingLocation, ItemFrame.class);
            itemFrame.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BYTE, (byte) 1);

            var playerHead = new ItemStack(Material.PLAYER_HEAD);
            var meta = (SkullMeta) playerHead.getItemMeta();
            Objects.requireNonNull(meta).setOwningPlayer(event.getEntity());
            playerHead.setItemMeta(meta);

            itemFrame.setItem(playerHead);
        }, 1);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        var block = event.getClickedBlock();
        if (block == null || block.getType() != Material.CHEST) {
            return;
        }

        var chest = (Chest) block.getState();
        if (!chest.getPersistentDataContainer().has(namespacedKey, PersistentDataType.BYTE)) {
            return;
        }

        for (var drop : chest.getBlockInventory()) {
            if (drop == null || !drop.getType().isItem() || drop.getType().isAir()) {
                continue;
            }
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        }

        block.setType(Material.AIR);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (isKeepInventoryChestItemFrameEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (!isKeepInventoryChestItemFrameEntity(event.getEntity())) {
            return;
        }

        if (event.getCause() == HangingBreakEvent.RemoveCause.OBSTRUCTION) {
            event.setCancelled(true);
        } else {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    private boolean isKeepInventoryChestItemFrameEntity(Entity entity) {
        if (!(entity instanceof ItemFrame itemFrame)) {
            return false;
        }

        return itemFrame.getPersistentDataContainer().has(namespacedKey, PersistentDataType.BYTE);
    }
}
