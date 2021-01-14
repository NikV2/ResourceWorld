package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.custom.CustomWorld;
import me.nik.resourceworld.utils.MiscUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetSpawn extends SubCommand {

    private final ResourceWorld plugin;

    public SetSpawn(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "setspawn";
    }

    @Override
    protected String getDescription() {
        return "Set the main world's spawn";
    }

    @Override
    protected String getSyntax() {
        return "/resource setspawn";
    }

    @Override
    protected String getPermission() {
        return Permissions.ADMIN.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    protected boolean canConsoleExecute() {
        return false;
    }

    @Override
    protected void perform(CommandSender sender, String[] args) {

        final Location spawnLocation = ((Player) sender).getLocation();

        final String playerWorldName = spawnLocation.getWorld().getName();

        for (CustomWorld rw : this.plugin.getResourceWorlds().values()) {
            if (rw.getName().equals(playerWorldName)) {
                sender.sendMessage(MsgType.INVALID_SPAWN.getMessage());
                return;
            }
        }

        this.plugin.getData().set("main_spawn", MiscUtils.locationToString(spawnLocation));
        this.plugin.saveData();
        this.plugin.reloadData();
        this.plugin.saveData();

        final String location = "X: " + spawnLocation.getX() + " Y: " + spawnLocation.getY() + " Z: " + spawnLocation.getZ();

        sender.sendMessage(MsgType.SPAWN_SET.getMessage().replace("%location%", location));
    }

    @Override
    protected List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}