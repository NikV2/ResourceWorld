package me.nik.resourceworld.tasks;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AlwaysDay extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getWorld(Config.Setting.WORLD_NAME.getString()).setTime(1000);
    }
}