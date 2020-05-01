package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldSettings extends Manager {
    public WorldSettings(ResourceWorld plugin) {
        super(plugin);

        if (configBoolean("world.settings.always_day")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (worldExists()) {
                        Bukkit.getWorld(configString("world.settings.world_name")).setTime(1000);
                    } else cancel();
                }
            }.runTaskTimer(plugin, 1200, 1200);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Item) return;
        if (e.getEntity() instanceof Player) return;
        if (e.getEntity() instanceof ArmorStand) return;
        if (e.getEntity() instanceof Projectile) return;
        if (e.getEntity() instanceof ItemFrame) return;
        if (configBoolean("world.settings.disable_entity_spawning") && e.getEntity().getWorld().getName().equalsIgnoreCase(configString("world.settings.world_name"))) {
            e.setCancelled(true);
        }
        if (configBoolean("nether_world.settings.disable_entity_spawning") && e.getEntity().getWorld().getName().equalsIgnoreCase(configString("nether_world.settings.world_name"))) {
            e.setCancelled(true);
        }
        if (configBoolean("end_world.settings.disable_entity_spawning") && e.getEntity().getWorld().getName().equalsIgnoreCase("end_world.settings.world_name")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (configBoolean("world.settings.disable_suffocation_damage") && world.equalsIgnoreCase(configString("world.settings.world_name")) && e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
        if (configBoolean("world.settings.disable_drowning_damage") && world.equalsIgnoreCase(configString("world.settings.world_name")) && e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            e.setCancelled(true);
        }
        if (configBoolean("nether_world.settings.disable_suffocation_damage") && world.equalsIgnoreCase(configString("nether_world.settings.world_name")) && e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
        if (configBoolean("end_world.settings.disable_suffocation_damage") && world.equalsIgnoreCase(configString("end_world.settings.world_name")) && e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
    }
}
