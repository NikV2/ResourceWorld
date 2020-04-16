package me.nik.resourceworld.utils;

import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.listeners.DisabledCmds;
import me.nik.resourceworld.listeners.OreRegen;
import me.nik.resourceworld.listeners.WorldSettings;

public class Initializer extends Manager {

    public void initialize() {
        if (configBoolean("disabled_commands.enabled")) {
            registerEvent(new DisabledCmds());
        }
        if (configBoolean("world.settings.block_regeneration.enabled")) {
            registerEvent(new OreRegen());
        }
        if (configBoolean("world.settings.disable_suffocation_damage") || configBoolean("world.settings.disable_drowning_damage") || configBoolean("world.settings.disable_entity_spawning")) {
            registerEvent(new WorldSettings());
        }
    }
}
