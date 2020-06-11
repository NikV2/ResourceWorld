package me.nik.resourceworld.listeners.suffocation;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Suffocation implements Listener {

    private final String overworld;

    public Suffocation(ResourceWorld plugin) {
        this.overworld = plugin.getConfig().getString("world.settings.world_name");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (!world.equalsIgnoreCase(overworld)) return;
        e.setCancelled(true);
    }
}