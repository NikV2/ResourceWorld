package me.nik.resourceworld.modules.impl.explosion;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Explosion extends ListenerModule {

    public Explosion(ResourceWorld plugin) {
        super(Config.Setting.WORLD_DISABLE_EXPLOSIONS.getBoolean(), plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        String world = entity.getWorld().getName();
        if (!world.equals(Config.Setting.WORLD_NAME.getString())) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageByExplosion(EntityDamageEvent e) {
        if (!(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) return;
        Entity entity = e.getEntity();
        String world = entity.getWorld().getName();
        if (!world.equals(Config.Setting.WORLD_NAME.getString())) return;
        e.setCancelled(true);
    }
}