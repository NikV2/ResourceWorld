package me.nik.resourceworld.utils;
import me.nik.resourceworld.ResourceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ResetTeleport {
    Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    public void resetTP(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if (p.getWorld().getName().equals(plugin.getConfig().getString("world_name"))) {
                Location loc = Bukkit.getWorld(plugin.getConfig().getString("fallback_world")).getSpawnLocation();
                p.teleport(loc);
            }
        }
    }
}
