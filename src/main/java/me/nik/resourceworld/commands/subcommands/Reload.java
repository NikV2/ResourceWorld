package me.nik.resourceworld.commands.subcommands;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Reload extends SubCommand {
    @Override
    public String getName() {
        return "Reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin";
    }

    @Override
    public String getSyntax() {
        return "/Resource Reload";
    }

    @Override
    public void perform(Player player, String[] args) {
        Plugin plugin = ResourceWorld.getPlugin(ResourceWorld.class);
        if (!player.hasPermission("rw.admin")) {
            player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("no_perm")));
        } else {
            if (args.length > 0) {
                player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("reloading")));
                plugin.getServer().getPluginManager().disablePlugin(ResourceWorld.getPlugin(ResourceWorld.class));
                plugin.getServer().getPluginManager().enablePlugin(ResourceWorld.getPlugin(ResourceWorld.class));
                player.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("reloaded")));
                System.gc();
                }
            }
        }
    }