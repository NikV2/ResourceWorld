package me.nik.resourceworld.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    protected abstract String getName();

    protected abstract String getDescription();

    protected abstract String getSyntax();

    protected abstract String getPermission();

    protected abstract boolean canConsoleExecute();

    protected abstract void perform(CommandSender sender, String[] args);

    protected abstract List<String> getSubcommandArguments(CommandSender sender, String[] args);
}
