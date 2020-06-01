package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldSettings implements Listener {

    private final String overworld = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");

    private final WorldUtils worldUtils = new WorldUtils();

    public WorldSettings(ResourceWorld plugin) {

        if (Config.get().getBoolean("world.settings.always_day")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (worldUtils.worldExists()) {
                        Bukkit.getWorld(overworld).setTime(1000);
                    } else cancel();
                }
            }.runTaskTimer(plugin, 1200, 1200);
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Item) return;
        if (e.getEntity() instanceof Player) return;
        if (e.getEntity() instanceof ArmorStand) return;
        if (e.getEntity() instanceof Projectile) return;
        if (e.getEntity() instanceof ItemFrame) return;
        if (Config.get().getBoolean("world.settings.disable_entity_spawning") && e.getEntity().getWorld().getName().equalsIgnoreCase(overworld)) {
            e.setCancelled(true);
        }
        if (Config.get().getBoolean("nether_world.settings.disable_entity_spawning") && e.getEntity().getWorld().getName().equalsIgnoreCase(nether)) {
            e.setCancelled(true);
        }
        if (Config.get().getBoolean("end_world.settings.disable_entity_spawning") && e.getEntity().getWorld().getName().equalsIgnoreCase(end)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        String world = p.getWorld().getName();
        if (Config.get().getBoolean("world.settings.disable_suffocation_damage") && world.equalsIgnoreCase(overworld) && e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
        if (Config.get().getBoolean("world.settings.disable_drowning_damage") && world.equalsIgnoreCase(overworld) && e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            e.setCancelled(true);
        }
        if (Config.get().getBoolean("nether_world.settings.disable_suffocation_damage") && world.equalsIgnoreCase(nether) && e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
        if (Config.get().getBoolean("end_world.settings.disable_suffocation_damage") && world.equalsIgnoreCase(end) && e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
    }
}
