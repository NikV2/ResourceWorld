package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class Spawn extends SubCommand {
    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getDescription() {
        return "Teleport to the Main World's Spawn";
    }

    @Override
    public String getSyntax() {
        return "/resource spawn";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            if (!player.hasPermission("rw.tp")) {
                player.sendMessage(Messenger.message("no_perm"));
            } else if (Bukkit.getWorld(Config.get().getString("world.settings.main_spawn_world")) == null) {
                player.sendMessage(Messenger.message("main_world_error"));
            } else {
                final Location loc = Bukkit.getWorld(Config.get().getString("world.settings.main_spawn_world")).getSpawnLocation();
                player.teleport(loc);
                player.sendMessage(Messenger.message("teleported_message"));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}