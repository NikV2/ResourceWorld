package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.tasks.UpdateChecker;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateReminder implements Listener {

    private final ResourceWorld plugin;

    public UpdateReminder(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("rw.admin")) return;

        new BukkitRunnable() {

            @Override
            public void run() {
                e.getPlayer().sendMessage(Messenger.message("update_found").replaceAll("%current%", plugin.getDescription().getVersion()).replaceAll("%new%", UpdateChecker.newVersion));
            }
        }.runTaskLaterAsynchronously(plugin, 40);
    }
}
