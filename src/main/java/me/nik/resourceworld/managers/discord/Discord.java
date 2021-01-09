package me.nik.resourceworld.managers.discord;

import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.TaskUtils;
import org.bukkit.Color;

import java.io.IOException;

public class Discord {

    private final String url = Config.Setting.SETTINGS_DISCORD_URL.getString();
    private final boolean enabled = Config.Setting.SETTINGS_DISCORD_ENABLED.getBoolean();

    private final String title;
    private final String description;
    private final Color color;

    public Discord(String title, String description, Color color) {
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public void sendNotification() {
        if (!enabled) return;
        if (url == null || url.isEmpty()) return;

        TaskUtils.taskLaterAsync(() -> {
            DiscordWebhook discord = new DiscordWebhook(url);
            discord.addEmbed(new DiscordWebhook.EmbedObject().setTitle(title)
                    .setDescription(description)
                    .setColor(color));
            try {
                discord.execute();
            } catch (IOException ignored) {
            }
        }, 20);
    }
}