package me.nik.resourceworld.modules.impl;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.Permissions;
import me.nik.resourceworld.modules.ListenerModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class DisabledCommands extends ListenerModule {

    public DisabledCommands(ResourceWorld plugin) {
        super((Config.Setting.WORLD_DISABLED_COMMANDS_ENABLED.getBoolean()
                || Config.Setting.NETHER_DISABLED_COMMANDS_ENABLED.getBoolean()
                || Config.Setting.END_DISABLED_COMMANDS_ENABLED.getBoolean()), plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();

        if (player.hasPermission(Permissions.ADMIN.getPermission()) || e.getMessage().equals("/")) return;

        String world = player.getWorld().getName();

        String message = e.getMessage();

        if (world.equals(Config.Setting.WORLD_NAME.getString())) {

            e.setCancelled(
                    Config.Setting.WORLD_DISABLED_COMMANDS_ENABLED.getBoolean()
                            && isDisabledCommand(Config.Setting.WORLD_DISABLED_COMMANDS_LIST.getStringList(), message)
            );

        } else if (world.equals(Config.Setting.NETHER_NAME.getString())) {

            e.setCancelled(
                    Config.Setting.NETHER_DISABLED_COMMANDS_ENABLED.getBoolean()
                            && isDisabledCommand(Config.Setting.NETHER_DISABLED_COMMANDS_LIST.getStringList(), message)
            );

        } else if (world.equals(Config.Setting.END_NAME.getString())) {

            e.setCancelled(
                    Config.Setting.END_DISABLED_COMMANDS_ENABLED.getBoolean()
                            && isDisabledCommand(Config.Setting.END_DISABLED_COMMANDS_LIST.getStringList(), message)
            );
        }

        if (e.isCancelled()) {
            player.sendMessage(MsgType.DISABLED_COMMAND.getMessage());
        }
    }

    private boolean isDisabledCommand(List<String> list, String cmd) {
        return list.stream().anyMatch(cmd::contains);
    }
}