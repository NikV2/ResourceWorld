package me.nik.resourceworld.commands.subcommands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nik.resourceworld.commands.CommandManager;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.Permissions;
import me.nik.resourceworld.utils.ChatUtils;
import me.nik.resourceworld.utils.LocationFinder;

public class TpPlayer extends SubCommand {

	private final LocationFinder locationFinder = new LocationFinder();

	@Override
	protected String getName() {
		return "tpplayer";
	}

	@Override
	protected String getDescription() {
		return "Randomly Teleport targeted player to the Resource World!";
	}

	@Override
	protected String getSyntax() {
		return "/resource tpplayer <player>";
	}

	@Override
	protected String getPermission() {
		return Permissions.ADMIN.getPermission();
	}

	@Override
	protected int maxArguments() {
		return 2;
	}

	@Override
	protected boolean canConsoleExecute() {
		return true;
	}

	@Override
	protected void perform(CommandSender sender, String[] args) {

		if (args.length == 2) {
			if (Bukkit.getPlayerExact(args[1]) != null) {
				Player targetPlayer = Bukkit.getPlayerExact(args[1]);
				String worldName = Config.Setting.WORLD_NAME.getString();
				World world = Bukkit.getWorld(worldName);

				sendColoredText(sender, MsgType.TELEPORTING_OTHER.getMessage().replaceAll("%player%", args[1]));
				sendColoredText(sender, MsgType.TELEPORTED_BY_ADMIN.getMessage());
				this.locationFinder.teleportSafely(Bukkit.getPlayerExact(args[1]), world);

			} else {
				sendColoredText(sender, MsgType.PLAYER_DONT_EXIST.getMessage().replaceAll("%player%", args[1]));
			}
		} else {
			CommandManager.helpMessage(sender);
		}
	}

	@Override
	protected List<String> getSubcommandArguments(CommandSender sender, String[] args) {

		return null;
	}

	private void sendColoredText(CommandSender sender, String message) {
		sender.sendMessage(ChatUtils.format(message));
	}

}
