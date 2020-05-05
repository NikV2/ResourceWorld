package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.entity.Player;

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
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("rw.admin")) {
            player.sendMessage(Messenger.message("no_perm"));
            return;
        }
        if (args.length == 1) {
            player.sendMessage(Messenger.message("reloading"));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            plugin.getServer().getPluginManager().enablePlugin(plugin);
            player.sendMessage(Messenger.message("reloaded"));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}