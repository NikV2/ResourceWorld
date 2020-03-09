package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.WorldDeleter;
import me.nik.resourceworld.utils.WorldGenerator;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetWorld extends BukkitRunnable {
    ResourceWorld plugin;

    public ResetWorld(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().broadcastMessage(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Resetting The World")));
        new WorldDeleter().deleteWorld();
        System.gc();
        new WorldGenerator().createWorld();
        plugin.getServer().broadcastMessage(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("World Has Been Reset")));
    }
}
