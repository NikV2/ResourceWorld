package me.nik.resourceworld.commands;
import me.nik.resourceworld.commands.subcommands.Menu;
import me.nik.resourceworld.commands.subcommands.Reload;
import me.nik.resourceworld.commands.subcommands.Teleport;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.ColourUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();
    public CommandManager(){
        subcommands.add(new Teleport());
        subcommands.add(new Reload());
        subcommands.add(new Menu());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColourUtils.format(Lang.get().getString("prefix")) + ColourUtils.format(Lang.get().getString("console_message")));
        }else{
            Player p = (Player) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        getSubcommands().get(i).perform(p, args);
                    }
                }
            } else {
                p.sendMessage(ChatColor.DARK_GREEN + "                        >> Resource World <<                  ");
                p.sendMessage(ChatColor.GREEN + "----------------------------------------------------");
                for (int i = 0; i < getSubcommands().size(); i++) {
                    p.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
                }
                p.sendMessage(ChatColor.GREEN + "----------------------------------------------------");
            }
        }
        return true;
    }
    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }
}
