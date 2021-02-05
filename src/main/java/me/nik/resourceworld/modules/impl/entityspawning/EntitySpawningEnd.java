package me.nik.resourceworld.modules.impl.entityspawning;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class EntitySpawningEnd extends ListenerModule {

    public EntitySpawningEnd(ResourceWorld plugin) {
        super(Config.Setting.END_DISABLE_ENTITY_SPAWNING.getBoolean(), plugin);
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Player) return;
        if (!entity.getWorld().getName().equals(Config.Setting.END_NAME.getString())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (!e.getWorld().getName().equals(Config.Setting.END_NAME.getString())) return;
        for (Entity entity : e.getChunk().getEntities()) {
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity ent = (LivingEntity) entity;
            if (ent instanceof Player) continue;
            ent.remove();
        }
    }
}