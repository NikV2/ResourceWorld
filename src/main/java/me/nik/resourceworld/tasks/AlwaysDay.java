package me.nik.resourceworld.tasks;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AlwaysDay extends BukkitRunnable {

    @Override
    public void run() {
        if (!WorldUtils.worldExists()) return;
        Bukkit.getWorld(Config.Setting.WORLD_NAME.getString()).setTime(1000);
    }
}