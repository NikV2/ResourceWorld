package me.nik.resourceworld.listeners.explosion;

import me.nik.resourceworld.files.Config;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionEnd implements Listener {

    private final String end = Config.get().getString("end_world.settings.world_name");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        String world = entity.getWorld().getName();
        if (!world.equalsIgnoreCase(end)) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageByExplosion(EntityDamageEvent e) {
        if (!(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) return;
        Entity entity = e.getEntity();
        String world = entity.getWorld().getName();
        if (!world.equalsIgnoreCase(end)) return;
        e.setCancelled(true);
    }
}
