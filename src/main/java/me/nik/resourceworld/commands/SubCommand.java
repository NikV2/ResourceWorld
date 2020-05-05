package me.nik.resourceworld.commands;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    protected abstract String getName();

    protected abstract String getDescription();

    protected abstract String getSyntax();

    protected abstract void perform(Player player, String[] args);

    protected abstract List<String> getSubcommandArguments(Player player, String[] args);
}
