package me.nik.resourceworld.listeners.blockregen;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BlockRegenNether implements Listener {

    private final ResourceWorld plugin;

    private final int delay = Config.get().getInt("nether_world.settings.block_regeneration.regeneration_delay") * 1200;
    private final String world = Config.get().getString("nether_world.settings.world_name");
    private final List<String> blocks = Config.get().getStringList("nether_world.settings.block_regeneration.blocks");

    public BlockRegenNether(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.getBlock().getWorld().getName().equalsIgnoreCase(world))
            return;
        final Material type = e.getBlock().getType();
        for (String block : blocks) {
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
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase(world))
            return;
        if (e.getPlayer().hasPermission("rw.admin")) return;
        Material type = e.getBlock().getType();
        for (String block : blocks) {
            if (type.toString().equalsIgnoreCase(block)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Messenger.message(MsgType.BLOCK_PLACE));
            }
        }
    }
}
