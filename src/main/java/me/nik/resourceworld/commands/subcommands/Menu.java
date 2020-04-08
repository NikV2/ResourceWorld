package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.GUIManager;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.entity.Player;

import java.util.List;

public class Menu extends SubCommand {
    private ResourceWorld plugin;

    public Menu(ResourceWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getDescription() {
        return "Open the Resource World Menu!";
    }

    @Override
    public String getSyntax() {
        return "/Resource Menu";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            if (!player.hasPermission("rw.admin")) {
                player.sendMessage(Messenger.message("no_perm"));
            } else {
                new GUIManager(plugin).openMainGUI(player);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}