package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Drowning implements Listener {

    private final String overworld;

    public Drowning(ResourceWorld plugin) {
        this.overworld = plugin.getConfig().getString("world.settings.world_name");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.DROWNING)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (!world.equalsIgnoreCase(overworld)) return;
        e.setCancelled(true);
    }
}
