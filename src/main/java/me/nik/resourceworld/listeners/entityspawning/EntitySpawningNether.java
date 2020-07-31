package me.nik.resourceworld.listeners.entityspawning;

import me.nik.resourceworld.files.Config;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class EntitySpawningNether implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Player) return;
        if (!entity.getWorld().getName().equalsIgnoreCase(Config.Setting.NETHER_NAME.getString())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (!e.getWorld().getName().equalsIgnoreCase(Config.Setting.NETHER_NAME.getString())) return;
        for (Entity entity : e.getChunk().getEntities()) {
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity ent = (LivingEntity) entity;
            if (ent instanceof Player) continue;
            ent.remove();
        }
    }
}