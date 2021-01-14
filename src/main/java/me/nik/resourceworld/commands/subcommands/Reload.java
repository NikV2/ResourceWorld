package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Reload extends SubCommand {

    private final ResourceWorld plugin;

    public Reload(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin";
    }

    @Override
    public String getSyntax() {
        return "/resource reload";
    }

    @Override
    public String getPermission() {
        return Permissions.ADMIN.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        plugin.onDisable();
        plugin.onEnable();
        sender.sendMessage(MsgType.RELOADED.getMessage());
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}