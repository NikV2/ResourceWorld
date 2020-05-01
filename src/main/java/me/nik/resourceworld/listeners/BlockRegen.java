package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockRegen extends Manager {
    private final int delay = configInt("world.settings.block_regeneration.regeneration_delay") * 1200;

    public BlockRegen(ResourceWorld plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.getBlock().getWorld().getName().equalsIgnoreCase(configString("world.settings.world_name"))) return;
        final Material type = e.getBlock().getType();
        for (String block : configStringList("world.settings.block_regeneration.blocks")) {
            if (type.toString().equalsIgnoreCase(block)) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        e.getBlock().setType(type);
                    }
                }.runTaskLater(plugin, delay);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase(configString("world.settings.world_name"))) return;
        if (e.getPlayer().hasPermission("rw.admin")) return;
        Material type = e.getBlock().getType();
        for (String block : configStringList("world.settings.block_regeneration.blocks")) {
            if (type.toString().equalsIgnoreCase(block)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Messenger.message("block_place"));
            }
        }
    }
}