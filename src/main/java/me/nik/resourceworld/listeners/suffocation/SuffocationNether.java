package me.nik.resourceworld.listeners.suffocation;

import me.nik.resourceworld.files.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class SuffocationNether implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (!world.equals(Config.Setting.NETHER_NAME.getString())) return;
        e.setCancelled(true);
    }
}