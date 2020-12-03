package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.custom.ResourceWorldType;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetNetherWorld extends BukkitRunnable {

    private final ResourceWorld plugin;

    public ResetNetherWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_NETHER).reset();
    }
}