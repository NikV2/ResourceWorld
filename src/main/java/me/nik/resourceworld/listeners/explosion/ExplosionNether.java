package me.nik.resourceworld.listeners.explosion;

import me.nik.resourceworld.files.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class ExplosionNether implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        String nether = Config.get().getString("nether_world.settings.world_name");
        if (!world.equalsIgnoreCase(nether)) return;
        e.setCancelled(true);
    }
}
