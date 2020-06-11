package me.nik.resourceworld.listeners.suffocation;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class SuffocationNether implements Listener {

    private final String nether;

    public SuffocationNether(ResourceWorld plugin) {
        this.nether = plugin.getConfig().getString("nether_world.settings.world_name");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (!world.equalsIgnoreCase(nether)) return;
        e.setCancelled(true);
    }
}
