package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.ResourceWorldType;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetEndWorld extends BukkitRunnable {

    private final ResourceWorld plugin;

    public ResetEndWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_END).reset();
    }
}