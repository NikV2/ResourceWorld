package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Drowning implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.DROWNING)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (!world.equals(Config.Setting.WORLD_NAME.getString())) return;
        e.setCancelled(true);
    }
}