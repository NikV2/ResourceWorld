package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.api.GUIManager;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.entity.Player;

import java.util.List;

public class Menu extends SubCommand {

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "Open the Resource World Menu!";
    }

    @Override
    public String getSyntax() {
        return "/resource menu";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("rw.admin")) {
            player.sendMessage(Messenger.message("no_perm"));
            return;
        }
        if (args.length == 1) {
            new GUIManager().openMainGUI(player);
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}