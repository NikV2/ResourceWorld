package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.tasks.ResetByCommand;
import me.nik.resourceworld.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Reset extends SubCommand {

    private final ResourceWorld plugin;

    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final String world = Config.get().getString("world.settings.world_name");
    private final String nether = Config.get().getString("nether_world.settings.world_name");
    private final String end = Config.get().getString("end_world.settings.world_name");

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
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("rw.admin")) {
            player.sendMessage(Messenger.message("no_perm"));
            return;
        }
        int timer = 10;
        if (args.length == 1) {
            if (worldExists(world)) {
                if (cooldown.containsKey(player.getUniqueId())) {
                    long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + timer) - (System.currentTimeMillis() / 1000);
                    player.sendMessage(Messenger.message("reset_cooldown").replaceAll("%seconds%", String.valueOf(secondsleft)));
                } else {
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    new ResetByCommand(plugin).executeReset();
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            cooldown.remove(player.getUniqueId());
                            cancel();
                        }
                    }.runTaskLaterAsynchronously(plugin, timer * 20);
                }
            } else {
                player.sendMessage(Messenger.message("not_exist"));
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("nether")) {
            if (worldExists(nether)) {
                if (cooldown.containsKey(player.getUniqueId())) {
                    long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + timer) - (System.currentTimeMillis() / 1000);
                    player.sendMessage(Messenger.message("reset_cooldown").replaceAll("%seconds%", String.valueOf(secondsleft)));
                } else {
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    new ResetByCommand(plugin).executeNetherReset();
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            cooldown.remove(player.getUniqueId());
                            cancel();
                        }
                    }.runTaskLaterAsynchronously(plugin, timer * 20);
                }
            } else {
                player.sendMessage(Messenger.message("not_exist"));
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("end")) {
            if (worldExists(end)) {
                if (cooldown.containsKey(player.getUniqueId())) {
                    long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + timer) - (System.currentTimeMillis() / 1000);
                    player.sendMessage(Messenger.message("reset_cooldown").replaceAll("%seconds%", String.valueOf(secondsleft)));
                } else {
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    new ResetByCommand(plugin).executeEndReset();
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            cooldown.remove(player.getUniqueId());
                            cancel();
                        }
                    }.runTaskLaterAsynchronously(plugin, timer * 20);
                }
            } else {
                player.sendMessage(Messenger.message("not_exist"));
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {

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
