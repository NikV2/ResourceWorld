package me.nik.resourceworld.managers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.utils.MiscUtils;
import org.bukkit.entity.Player;

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
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        switch (identifier) {
            case "world":
                if (Config.Setting.WORLD_STORE_TIME.getBoolean()) {
                    return MiscUtils.getDurationBreakdown((Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 3600000 + plugin.getData().getLong("world.millis")) - System.currentTimeMillis());
                } else {
                    return MiscUtils.getDurationBreakdown((Config.Setting.WORLD_RESETS_INTERVAL.getInt() * 3600000 + plugin.getData().getLong("world.papi")) - System.currentTimeMillis());
                }
            case "nether":
                if (Config.Setting.NETHER_STORE_TIME.getBoolean()) {
                    return MiscUtils.getDurationBreakdown((Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 3600000 + plugin.getData().getLong("world.millis")) - System.currentTimeMillis());
                } else {
                    return MiscUtils.getDurationBreakdown((Config.Setting.NETHER_RESETS_INTERVAL.getInt() * 3600000 + plugin.getData().getLong("world.papi")) - System.currentTimeMillis());
                }
            case "end":
                if (Config.Setting.END_STORE_TIME.getBoolean()) {
                    return MiscUtils.getDurationBreakdown((Config.Setting.END_RESETS_INTERVAL.getInt() * 3600000 + plugin.getData().getLong("world.millis")) - System.currentTimeMillis());
                } else {
                    return MiscUtils.getDurationBreakdown((Config.Setting.END_RESETS_INTERVAL.getInt() * 3600000 + plugin.getData().getLong("world.papi")) - System.currentTimeMillis());
                }
        }
        return null;
    }
}