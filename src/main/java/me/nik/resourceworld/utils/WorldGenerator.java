package me.nik.resourceworld.utils;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldGenerator {
    World world;

    public void createWorld() {
        try {
            System.out.println(Lang.get().getString("Generating"));
            String wname = Config.get().getString("WorldName");
            String wtype = Config.get().getString("WorldType");
            Boolean gstr = Config.get().getBoolean("GenerateStructures");
            WorldCreator wc = new WorldCreator(wname);
            wc.type(WorldType.valueOf(wtype));
            wc.generateStructures(gstr);
            world = wc.createWorld();
            Bukkit.getServer().getWorld(wname).save();
            System.out.println(Lang.get().getString("Generated"));
        }catch (NullPointerException e){
            //Nothing
        }
    }
}
