package me.nik.resourceworld.managers;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker extends BukkitRunnable implements Listener {

    private final ResourceWorld plugin;
    private String newVersion;

    public UpdateChecker(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            newVersion = readLines();
        } catch (IOException e) {
            plugin.getLogger().warning("Couldn't check for updates, Is the server connected to the internet?");
            return;
        }

        if (!plugin.getDescription().getVersion().equals(newVersion)) {
            Messenger.consoleMessage(MsgType.UPDATE_FOUND.getMessage().replaceAll("%current%", plugin.getDescription().getVersion()).replaceAll("%new%", newVersion));
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            Messenger.consoleMessage(MsgType.UPDATE_NOT_FOUND.getMessage());
        }
    }

    private String readLines() throws IOException {
        URLConnection connection = new URL("https://raw.githubusercontent.com/NikV2/ResourceWorld/master/version.txt").openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        final String line = reader.readLine();

        reader.close();

        return line;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission(Permissions.ADMIN.getPermission())) return;

        e.getPlayer().sendMessage(MsgType.UPDATE_FOUND.getMessage()
                .replace("%current%", plugin.getDescription().getVersion())
                .replace("%new%", newVersion));
    }
}