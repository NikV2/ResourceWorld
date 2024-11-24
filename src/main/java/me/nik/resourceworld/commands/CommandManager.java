package me.nik.resourceworld.commands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.subcommands.Menu;
import me.nik.resourceworld.commands.subcommands.Reload;
import me.nik.resourceworld.commands.subcommands.Reset;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.managers.MsgType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    private static final String INFO_MESSAGE = MsgType.PREFIX.getMessage()
            + ChatColor.GRAY + "You're running " + ChatColor.WHITE
            + ResourceWorld.getInstance().getDescription().getName() + ChatColor.GRAY + " version "
            + ChatColor.GREEN + "v" + ResourceWorld.getInstance().getDescription().getVersion()
            + ChatColor.GRAY + " by" + ChatColor.WHITE + " Nik";
    private final List<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(ResourceWorld plugin) {
        subcommands.add(new Teleport());
        subcommands.add(new Reload(plugin));
        subcommands.add(new Menu(plugin));
        subcommands.add(new Reset(plugin));
    }

    public List<SubCommand> getSubcommands() {
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
                    return getSubcommands().get(i).getSubcommandArguments(sender, args);
                }
            }
        }
        return null;
    }

    private void helpMessage(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MsgType.PREFIX.getMessage() + ChatColor.GRAY + "Available Commands");
        sender.sendMessage("");
        this.subcommands.stream().filter(subCommand -> sender.hasPermission(subCommand.getPermission()))
                .forEach(subCommand -> sender.sendMessage(ChatColor.GREEN
                        + subCommand.getSyntax()
                        + ChatColor.DARK_GRAY + " - "
                        + ChatColor.GRAY + subCommand.getDescription()));
        sender.sendMessage("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {

            for (int i = 0; i < getSubcommands().size(); i++) {

                final SubCommand subCommand = getSubcommands().get(i);

                if (args[0].equalsIgnoreCase(subCommand.getName())) {

                    if (!subCommand.canConsoleExecute() && sender instanceof ConsoleCommandSender) {

                        sender.sendMessage(MsgType.CONSOLE_MESSAGE.getMessage());

                        return true;
                    }

                    if (!sender.hasPermission(subCommand.getPermission())) {

                        sender.sendMessage(MsgType.NO_PERMISSION.getMessage());

                        return true;
                    }

                    if (args.length < subCommand.maxArguments()) {

                        helpMessage(sender);

                        return true;
                    }

                    subCommand.perform(sender, args);

                    return true;
                }

                if (args[0].equalsIgnoreCase("help")) {

                    helpMessage(sender);

                    return true;
                }
            }

        } else {

            sender.sendMessage(INFO_MESSAGE);

            return true;
        }

        helpMessage(sender);

        return true;
    }
}