package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.custom.CustomWorld;
import me.nik.resourceworld.utils.MiscUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Spawn extends SubCommand {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    private final ResourceWorld plugin;

    public Spawn(ResourceWorld plugin) {
        this.plugin = plugin;
    }

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
        return Permissions.TELEPORT.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    public boolean canConsoleExecute() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        Player p = (Player) sender;

        final String playerWorld = p.getWorld().getName();

        for (CustomWorld rw : this.plugin.getResourceWorlds().values()) {
            if (!rw.getName().equals(playerWorld)) {
                p.sendMessage(MsgType.RESOURCEWORLD_SPAWN_TELEPORT.getMessage());
                return;
            }
        }

        Location spawn = MiscUtils.stringToLocation(this.plugin.getData().getString("main_spawn"));

        if (spawn == null) {
            p.sendMessage(MsgType.MAIN_WORLD_ERROR.getMessage());
            return;
        }

        if (!p.hasPermission(Permissions.ADMIN.getPermission())) {
            final UUID uuid = p.getUniqueId();
            if (cooldown.containsKey(uuid)) {
                long secondsleft = ((cooldown.get(uuid) / 1000) + Config.Setting.SETTINGS_SPAWN_COOLDOWN.getInt()) - (System.currentTimeMillis() / 1000);
                if (secondsleft > 0) {
                    p.sendMessage(MsgType.COOLDOWN_SPAWN.getMessage().replace("%seconds%", String.valueOf(secondsleft)));
                    return;
                }
                cooldown.remove(uuid);
            }

            cooldown.put(uuid, System.currentTimeMillis());
        }

        p.teleport(spawn);
        p.sendMessage(MsgType.TELEPORTED_MESSAGE.getMessage());
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}