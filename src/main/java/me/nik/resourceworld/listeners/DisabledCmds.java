package me.nik.resourceworld.listeners;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class DisabledCmds implements Listener {

    private final String world = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");
    private final List<String> commands = Config.get().getStringList("disabled_commands.commands");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void disableWorldCommands(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("rw.admin")) return;
        if (isInWorld(p)) {
            if (e.getMessage().equals("/")) return;
            for (String cmd : commands) {
                if (e.getMessage().contains(cmd)) {
                    e.setCancelled(true);
                    p.sendMessage(Messenger.message(MsgType.DISABLED_COMMAND));
                }
            }
        }
    }

    private boolean isInWorld(Player player) {
        if (player.getWorld().getName().equalsIgnoreCase(world)) {
            return true;
        } else if (player.getWorld().getName().equalsIgnoreCase(nether)) {
            return true;
        } else return player.getWorld().getName().equalsIgnoreCase(end);
    }
}
