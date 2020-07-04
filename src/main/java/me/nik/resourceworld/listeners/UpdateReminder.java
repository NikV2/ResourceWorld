package me.nik.resourceworld.listeners;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.tasks.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateReminder implements Listener {

    private final ResourceWorld plugin;
    private final UpdateChecker updateChecker;

    public UpdateReminder(ResourceWorld plugin, UpdateChecker updateChecker) {
        this.plugin = plugin;
        this.updateChecker = updateChecker;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("rw.admin")) return;
        String newVersion = updateChecker.getNewVersion();
        e.getPlayer().sendMessage(MsgType.UPDATE_FOUND.getMessage().replaceAll("%current%", plugin.getDescription().getVersion()).replaceAll("%new%", newVersion));
    }
}