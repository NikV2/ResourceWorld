package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AlwaysDay extends BukkitRunnable {

    private final String overworld;

    public AlwaysDay(ResourceWorld plugin) {
        this.overworld = plugin.getConfig().getString("world.settings.world_name");
    }

    @Override
    public void run() {
        if (!WorldUtils.worldExists()) return;
        Bukkit.getWorld(overworld).setTime(1000);
    }
}
