package me.nik.resourceworld.tasks;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.ResetTeleport;
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
        new ResetTeleport().resetTP();
        plugin.getServer().broadcastMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("resetting_the_world")));
        new WorldDeleter().deleteWorld();
        new WorldGenerator().createWorld();
        System.gc();
        plugin.getServer().broadcastMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("world_has_been_reset")));
    }
}
