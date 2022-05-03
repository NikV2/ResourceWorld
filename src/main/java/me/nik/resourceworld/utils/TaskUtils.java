package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.custom.ResourceWorldException;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class TaskUtils {

    private TaskUtils() {
        throw new ResourceWorldException("This is a static class dummy!");
    }

    public static BukkitTask task(Runnable runnable) {
        return Bukkit.getScheduler().runTask(ResourceWorld.getInstance(), runnable);
    }

    public static BukkitTask taskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(ResourceWorld.getInstance(), runnable, delay);
    }
}