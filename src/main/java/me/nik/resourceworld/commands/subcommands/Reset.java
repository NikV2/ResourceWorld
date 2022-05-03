package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.api.ResourceWorldType;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.Permissions;
import me.nik.resourceworld.managers.custom.CustomWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Reset extends SubCommand {

    private final ResourceWorld plugin;

    private long lastResetMillis;

    public Reset(ResourceWorld plugin) {
        this.plugin = plugin;
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

        //General check to make sure they don't try to mess with this.
        if (System.currentTimeMillis() - this.lastResetMillis < 10000L) return;

        this.lastResetMillis = System.currentTimeMillis();

        CustomWorld resourceWorld = null;

        switch (args.length) {

            case 1:

                resourceWorld = this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_WORLD);

                break;

            case 2:

                switch (args[1].toLowerCase()) {

                    case "nether":

                        resourceWorld = this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_NETHER);

                        break;

                    case "end":

                        resourceWorld = this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_END);

                        break;
                }

                break;
        }

        if (resourceWorld == null) {

            sender.sendMessage(MsgType.NOT_EXIST.getMessage());

        } else resourceWorld.reset();
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        if (args.length == 2) {

            List<String> worlds = new ArrayList<>();

            if (Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()) != null) {
                worlds.add("nether");
            }

            if (Bukkit.getWorld(Config.Setting.END_NAME.getString()) != null) {
                worlds.add("end");
            }

            return worlds;
        }

        return null;
    }
}