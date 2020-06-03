package me.nik.resourceworld.tasks;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AlwaysDay extends BukkitRunnable {

    private final String overworld = Config.get().getString("world.settings.world_name");

    @Override
    public void run() {
        if (!WorldUtils.worldExists()) return;
        Bukkit.getWorld(overworld).setTime(1000);
    }
}
