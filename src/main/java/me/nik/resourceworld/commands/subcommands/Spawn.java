package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
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
    public String getPermission() {
        return "rw.tp";
    }

    @Override
    public boolean canConsoleExecute() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Player player = (Player) sender;
            if (Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()) == null) {
                player.sendMessage(MsgType.MAIN_WORLD_ERROR.getMessage());
            } else {
                final Location loc = Bukkit.getWorld(Config.Setting.SETTINGS_SPAWN_WORLD.getString()).getSpawnLocation();
                player.teleport(loc);
                player.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}