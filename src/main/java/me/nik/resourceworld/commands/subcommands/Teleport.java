package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Teleport extends SubCommand {
    ResourceWorld plugin = ResourceWorld.getPlugin(ResourceWorld.class);
    private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private int cdtime = Config.get().getInt("teleport.settings.cooldown");
    private HashMap<UUID, Long> delay = new HashMap<UUID, Long>();
    private int delaytime = Config.get().getInt("teleport.settings.delay");

    @Override
    public String getName() {
        return "TP";
    }

    @Override
    public String getDescription() {
        return "Randomly Teleport To The Resource World!";
    }

    @Override
    public String getSyntax() {
        return "/Resource TP";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            if (!Config.get().getBoolean("settings.enabled")) {
                player.sendMessage(Messenger.message("not_exist"));
            } else if (!player.hasPermission("rw.tp")) {
                player.sendMessage(Messenger.message("no_perm"));
            } else if (cooldown.containsKey(player.getUniqueId())) {
                long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + cdtime) - (System.currentTimeMillis() / 1000);
                if (secondsleft > 0) {
                    player.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("cooldown_message")) + secondsleft + " Seconds"));
                } else {
                    player.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("teleport_delay")) + Config.get().getInt("teleport.settings.delay") + " Seconds"));
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    final Player p = player;
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            World world = Bukkit.getWorld(Config.get().getString("world.settings.world_name"));
                            p.teleport(new TeleportUtils().generateLocation(world));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Config.get().getString("teleport.settings.effects.effect")), Config.get().getInt("teleport.settings.effects.duration") * 20, Config.get().getInt("teleport.settings.effects.amplifier")));
                            cancel();
                        }
                    }.runTaskLater(plugin, Config.get().getInt("teleport.settings.delay") * 20);
                }
            } else {
                player.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("teleport_delay")) + Config.get().getInt("teleport.settings.delay") + " Seconds"));
                cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                final Player p = player;
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        World world = Bukkit.getWorld(Config.get().getString("world.settings.world_name"));
                        p.teleport(new TeleportUtils().generateLocation(world));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Config.get().getString("teleport.settings.effects.effect")), Config.get().getInt("teleport.settings.effects.duration") * 20, Config.get().getInt("teleport.settings.effects.amplifier")));
                        cancel();
                    }
                }.runTaskLater(plugin, Config.get().getInt("teleport.settings.delay") * 20);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}