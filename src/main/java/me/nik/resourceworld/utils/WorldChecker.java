package me.nik.resourceworld.utils;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import org.bukkit.Bukkit;

public class WorldChecker {

    public void wCheck() {
        String wname = Config.get().getString("WorldName");
        boolean worldExists = Bukkit.getWorld(wname).getWorldFolder().exists();
        if (!worldExists) {
            System.out.println(Lang.get().getString("NotFound"));
            new WorldGenerator().createWorld();
        } else {
            System.out.println(Lang.get().getString("Deleting"));
            new WorldUnloader().unloadWorld();
        }
    }
}