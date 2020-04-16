package me.nik.resourceworld.listeners;

import me.nik.resourceworld.api.Manager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OreRegen extends Manager {
    private final int delay = configInt("world.settings.block_regeneration.regeneration_delay") * 1200;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOreBreak(BlockBreakEvent e) {
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
}