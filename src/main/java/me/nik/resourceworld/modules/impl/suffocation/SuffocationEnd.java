package me.nik.resourceworld.modules.impl.suffocation;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class SuffocationEnd extends ListenerModule {

    public SuffocationEnd(ResourceWorld plugin) {
        super(Config.Setting.END_DISABLE_SUFFOCATION.getBoolean(), plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (!world.equals(Config.Setting.END_NAME.getString())) return;
        e.setCancelled(true);
    }
}