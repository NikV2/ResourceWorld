package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.tasks.ResetByCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Reset extends SubCommand {

    private final ResetByCommand resetByCommand;

    public Reset(ResourceWorld plugin) {
        this.resetByCommand = new ResetByCommand(plugin);
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Reset the Resource World!";
    }

    @Override
    public String getSyntax() {
        return "/resource reset <nether, end>";
    }

    @Override
    public String getPermission() {
        return Permissions.ADMIN;
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
        if (args.length == 1) {
            if (worldExists(Config.Setting.WORLD_NAME.getString())) {
                resetByCommand.executeReset();
            } else {
                sender.sendMessage(MsgType.NOT_EXIST.getMessage());
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("nether")) {
            if (worldExists(Config.Setting.NETHER_NAME.getString())) {
                resetByCommand.executeNetherReset();
            } else {
                sender.sendMessage(MsgType.NOT_EXIST.getMessage());
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("end")) {
            if (worldExists(Config.Setting.END_NAME.getString())) {
                resetByCommand.executeEndReset();
            }
        } else {
            sender.sendMessage(MsgType.NOT_EXIST.getMessage());
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        if (args.length == 2) {
            List<String> worlds = new ArrayList<>();
            if (worldExists(Config.Setting.NETHER_NAME.getString())) {
                worlds.add("nether");
            }
            if (worldExists(Config.Setting.END_NAME.getString())) {
                worlds.add("end");
            }
            return worlds;
        }

        return null;
    }

    private boolean worldExists(String world) {
        return Bukkit.getWorld(world) != null;
    }
}