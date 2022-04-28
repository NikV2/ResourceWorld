package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.gui.PlayerMenuUtility;
import me.nik.resourceworld.gui.menus.MainGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Menu extends SubCommand {

    private final ResourceWorld plugin;

    public Menu(ResourceWorld plugin) {
        this.plugin = plugin;
    }

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
    public String getPermission() {
        return Permissions.ADMIN.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    public boolean canConsoleExecute() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        new MainGui(new PlayerMenuUtility((Player) sender), plugin).open();
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}