package me.nik.resourceworld.listeners.blockregen;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockRegenPlace implements Listener {

    private final String world;
    private final List<String> blocks;

    public BlockRegenPlace(ResourceWorld plugin) {
        this.world = plugin.getConfig().getString("world.settings.world_name");
        this.blocks = plugin.getConfig().getStringList("world.settings.block_regeneration.blocks");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase(world)) return;
        if (e.getPlayer().hasPermission("rw.admin")) return;
        Material type = e.getBlock().getType();
        for (String block : blocks) {
            if (type.toString().equalsIgnoreCase(block)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(MsgType.BLOCK_PLACE.getMessage());
            }
        }
    }
}