package me.nik.resourceworld.commands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Menu;
import me.nik.resourceworld.commands.subcommands.Reload;
import me.nik.resourceworld.commands.subcommands.Reset;
import me.nik.resourceworld.commands.subcommands.Spawn;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    private final ResourceWorld plugin;

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(ResourceWorld plugin) {
        this.plugin = plugin;
        subcommands.add(new Teleport(plugin));
        subcommands.add(new Reload(plugin));
        subcommands.add(new Menu(plugin));
        subcommands.add(new Reset(plugin));
        subcommands.add(new Spawn());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                helpMessage(sender);
            } else if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(Messenger.message("reloading"));
                sender.getServer().getPluginManager().disablePlugin(plugin);
                sender.getServer().getPluginManager().enablePlugin(plugin);
                sender.sendMessage(Messenger.message("reloaded"));
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                helpMessage(sender);
                return true;
            } else {
                sender.sendMessage(Messenger.message("console_message"));
                return true;
            }
        } else {
            Player p = (Player) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        getSubcommands().get(i).perform(p, args);
                        return true;
                    } else if (args[0].equalsIgnoreCase("help")) {
                        helpMessage(p);
                        return true;
                    }
                }
            } else {
                pluginInfo(p);
                return true;
            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArgs = new ArrayList<>();
            for (int i = 0; i < getSubcommands().size(); i++) {
                subcommandsArgs.add(getSubcommands().get(i).getName());
            }
            return subcommandsArgs;
        } else if (args.length == 2) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return getSubcommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }
        return null;
    }

    private void helpMessage(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(Messenger.prefix(ChatColor.GRAY + "Available Commands"));
        sender.sendMessage("");
        for (int i = 0; i < getSubcommands().size(); i++) {
            sender.sendMessage(ChatColor.GREEN + getSubcommands().get(i).getSyntax() + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + getSubcommands().get(i).getDescription());
        }
        sender.sendMessage("");
    }

    private void pluginInfo(CommandSender sender) {
        sender.sendMessage(Messenger.prefix(ChatColor.GRAY + "You're running " + ChatColor.WHITE + plugin.getDescription().getName() + ChatColor.GRAY + " version " + ChatColor.GREEN + "v" + plugin.getDescription().getVersion() + ChatColor.GRAY + " by" + ChatColor.WHITE + " Nik"));
    }
}
