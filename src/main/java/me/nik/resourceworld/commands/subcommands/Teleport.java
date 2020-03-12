package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import me.nik.resourceworld.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class Teleport extends SubCommand {
    ArrayList<String> cooldown = new ArrayList<>();

    @Override
    public String getName() {
        return "TP";
    }

    @Override
    public String getDescription() {
        return "Randomly Teleport To The Resource World!";
    }

    @Override
    public String getSyntax() {
        return "/Resource TP";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("rw.tp")) {
            player.sendMessage(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("No Perm")));
        }else if (cooldown.contains(player.getName())) {
            player.sendMessage(ColourUtils.format(Lang.get().getString("Prefix")) + ColourUtils.format(Lang.get().getString("Cooldown Message")));
        }else{
            Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
            if (args.length > 0) {
                World world = Bukkit.getWorld(plugin.getConfig().getString("World Name"));
                player.teleport(new TeleportUtils().generateLocation(world));
                cooldown.add(player.getName());
                BukkitScheduler scheduler = plugin.getServer().getScheduler();
                    scheduler.runTaskLaterAsynchronously(ResourceWorld.getPlugin(ResourceWorld.class), new Runnable() {
                        @Override
                        public void run() {
                            cooldown.remove(player.getName());
                        }
                    }, plugin.getConfig().getInt("Teleport Cooldown") * 20);
                }
            }
        }
    }
