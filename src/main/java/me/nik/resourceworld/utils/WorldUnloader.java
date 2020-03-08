package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import org.bukkit.Bukkit;

public class WorldUnloader {
    public void unloadWorld() {
        String wname = Config.get().getString("WorldName");
        boolean isCreated = Bukkit.getWorld(wname).getWorldFolder().exists();
        if (isCreated) {
                try {
                    Bukkit.unloadWorld(wname, false);
                    new WorldDeleter().deleteWorld();
                }catch (NullPointerException e){
                    //Nothing
                }
            }else{
            new WorldGenerator().createWorld();
        }
    }
}