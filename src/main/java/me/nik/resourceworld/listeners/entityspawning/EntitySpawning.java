package me.nik.resourceworld.listeners.entityspawning;

import me.nik.resourceworld.files.Config;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawning implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Item) return;
        if (entity instanceof Player) return;
        if (entity instanceof ArmorStand) return;
        if (entity instanceof Projectile) return;
        if (entity instanceof ItemFrame) return;
        String world = Config.get().getString("world.settings.world_name");
        if (!entity.getWorld().getName().equalsIgnoreCase(world)) return;
        e.setCancelled(true);
    }
}
