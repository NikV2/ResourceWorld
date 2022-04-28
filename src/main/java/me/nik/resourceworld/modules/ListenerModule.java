package me.nik.resourceworld.modules;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class ListenerModule implements Listener {

    protected final ResourceWorld plugin;
    private final boolean enabled;

    public ListenerModule(boolean enabled, ResourceWorld plugin) {
        this.enabled = enabled;
        this.plugin = plugin;
    }

    public void load() {
        if (!this.enabled) return;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void shutdown() {
        if (!this.enabled) return;
        HandlerList.unregisterAll(this);
    }
}