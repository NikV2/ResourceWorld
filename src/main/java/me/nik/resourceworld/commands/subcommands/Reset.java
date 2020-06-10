package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.tasks.ResetByCommand;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Reset extends SubCommand {

    private final String world = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");
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
        return "rw.admin";
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (worldExists(world)) {
                resetByCommand.executeReset();
            } else {
                sender.sendMessage(Messenger.message(MsgType.NOT_EXIST));
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("nether")) {
            if (worldExists(nether)) {
                resetByCommand.executeNetherReset();
            } else {
                sender.sendMessage(Messenger.message(MsgType.NOT_EXIST));
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("end")) {
            if (worldExists(end)) {
                resetByCommand.executeEndReset();
            }
        } else {
            sender.sendMessage(Messenger.message(MsgType.NOT_EXIST));
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        if (args.length == 2) {
            List<String> worlds = new ArrayList<>();
            if (worldExists(nether)) {
                worlds.add("nether");
            }
            if (worldExists(end)) {
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
