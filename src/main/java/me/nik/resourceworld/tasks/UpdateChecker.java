package me.nik.resourceworld.tasks;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.listeners.UpdateReminder;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker extends BukkitRunnable {

    private final ResourceWorld plugin;

    public UpdateChecker(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    private String newVersion;

    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=75994").openConnection();
            String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

            if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                newVersion = version;
                Messenger.consoleMessage(MsgType.UPDATE_FOUND.getMessage().replaceAll("%current%", plugin.getDescription().getVersion()).replaceAll("%new%", newVersion));
                plugin.registerEvent(new UpdateReminder(plugin, this));
            } else {
                Messenger.consoleMessage(MsgType.UPDATE_NOT_FOUND.getMessage());
            }
        } catch (IOException ignored) {
        }
    }

    public String getNewVersion() {
        return newVersion;
    }
}