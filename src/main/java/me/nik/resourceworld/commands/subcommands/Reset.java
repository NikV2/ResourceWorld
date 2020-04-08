package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.tasks.ResetByCommand;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Reset extends SubCommand {
    private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private final int cdtime = 60;

    private ResourceWorld plugin;

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
        return "/resource reset";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            if (!player.hasPermission("rw.admin")) {
                player.sendMessage(Messenger.message("no_perm"));
            } else if (new WorldUtils().worldExists()) {
                if (cooldown.containsKey(player.getUniqueId())) {
                    long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + cdtime) - (System.currentTimeMillis() / 1000);
                    player.sendMessage(Messenger.message("reset_cooldown") + secondsleft + " Seconds.");
                } else {
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    new ResetByCommand(plugin).executeReset();
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            cooldown.remove(player.getUniqueId());
                            cancel();
                        }
                    }.runTaskLaterAsynchronously(plugin, cdtime * 20);
                }
            } else {
                player.sendMessage(Messenger.message("not_exist"));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
