package me.nik.resourceworld.managers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.MiscUtils;
import org.bukkit.OfflinePlayer;

public class PapiHook extends PlaceholderExpansion {

    private final ResourceWorld plugin;

    public PapiHook(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "rw";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        switch (identifier) {
            case "world":
                if (!Config.Setting.WORLD_ENABLED.getBoolean() && !Config.Setting.WORLD_RESETS_ENABLED.getBoolean())
                    return "";
                if (Config.Setting.WORLD_STORE_TIME.getBoolean()) {
                    return MiscUtils.getDurationBreakdown((Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 3600000L + plugin.getData().getLong("world.millis")) - System.currentTimeMillis());
                } else {
                    return MiscUtils.getDurationBreakdown((Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 3600000L + plugin.getData().getLong("world.papi")) - System.currentTimeMillis());
                }
            case "nether":
                if (!Config.Setting.NETHER_ENABLED.getBoolean() && !Config.Setting.NETHER_RESETS_ENABLED.getBoolean())
                    return "";
                if (Config.Setting.NETHER_STORE_TIME.getBoolean()) {
                    return MiscUtils.getDurationBreakdown((Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 3600000L + plugin.getData().getLong("world.millis")) - System.currentTimeMillis());
                } else {
                    return MiscUtils.getDurationBreakdown((Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 3600000L + plugin.getData().getLong("world.papi")) - System.currentTimeMillis());
                }
            case "end":
                if (!Config.Setting.END_ENABLED.getBoolean() && !Config.Setting.END_RESETS_ENABLED.getBoolean())
                    return "";
                if (Config.Setting.END_STORE_TIME.getBoolean()) {
                    return MiscUtils.getDurationBreakdown((Config.Setting.END_RESETS_INTERVAL.getInt() * 3600000L + plugin.getData().getLong("world.millis")) - System.currentTimeMillis());
                } else {
                    return MiscUtils.getDurationBreakdown((Config.Setting.END_RESETS_INTERVAL.getInt() * 3600000L + plugin.getData().getLong("world.papi")) - System.currentTimeMillis());
                }
        }
        return null;
    }
}