package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.TeleportUtils;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Teleport extends SubCommand {
    private final HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private final int cdtime = Config.get().getInt("teleport.settings.cooldown");
    private final HashMap<UUID, Long> delay = new HashMap<UUID, Long>();
    final private int delaytime = Config.get().getInt("teleport.settings.delay");
    final private PotionEffect effect = new PotionEffect(PotionEffectType.getByName(Config.get().getString("teleport.settings.effects.effect")), Config.get().getInt("teleport.settings.effects.duration") * 20, Config.get().getInt("teleport.settings.effects.amplifier"));
    final private World world = Bukkit.getWorld(Config.get().getString("world.settings.world_name"));
    final private int volume = Config.get().getInt("teleport.settings.sounds.volume");
    final private int pitch = Config.get().getInt("teleport.settings.sounds.pitch");

    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public String getDescription() {
        return "Randomly Teleport To The Resource World!";
    }

    @Override
    public String getSyntax() {
        return "/resource tp";
    }

    @Override
    public void perform(Player player, String[] args) {
        final UUID playerID = player.getUniqueId();
        if (args.length == 1) {
            if (!new WorldUtils().worldExists()) {
                player.sendMessage(Messenger.message("not_exist"));
            } else if (!player.hasPermission("rw.tp")) {
                player.sendMessage(Messenger.message("no_perm"));
            } else if (Config.get().getInt("teleport.settings.delay") < 1) {
                player.teleport(new TeleportUtils().generateLocation(world));
                player.addPotionEffect(effect);
                if (isSoundsEnabled()) {
                    try {
                        player.playSound(player.getLocation(), Sound.valueOf(Config.get().getString("teleport.settings.sounds.sound")), volume, pitch);
                    } catch (IllegalArgumentException ignored) {
                        System.out.println(Messenger.prefix(Messenger.format("Your current Teleportation sound does not exist on your Server Version! Please try setting a valid Sound Effect.")));
                    }
                }
            } else if (cooldown.containsKey(playerID)) {
                long secondsleft = ((cooldown.get(playerID) / 1000) + cdtime) - (System.currentTimeMillis() / 1000);
                if (secondsleft > 0) {
                    player.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("cooldown_message")) + secondsleft + " Seconds"));
                } else {
                    player.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("teleport_delay")) + delaytime + " Seconds"));
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    final Player p = player;
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            p.teleport(new TeleportUtils().generateLocation(world));
                            p.addPotionEffect(effect);
                            if (isSoundsEnabled()) {
                                try {
                                    player.playSound(player.getLocation(), Sound.valueOf(Config.get().getString("teleport.settings.sounds.sound")), volume, pitch);
                                } catch (IllegalArgumentException ignored) {
                                    System.out.println(Messenger.prefix(Messenger.format("Your current Teleportation sound does not exist on your Server Version! Please try setting a valid Sound Effect.")));
                                }
                            }
                            cancel();
                        }
                    }.runTaskLater(plugin, delaytime * 20);
                }
            } else {
                player.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("teleport_delay")) + delaytime + " Seconds"));
                cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                final Player p = player;
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        p.teleport(new TeleportUtils().generateLocation(world));
                        p.addPotionEffect(effect);
                        if (isSoundsEnabled()) {
                            try {
                                player.playSound(player.getLocation(), Sound.valueOf(Config.get().getString("teleport.settings.sounds.sound")), volume, pitch);
                            } catch (IllegalArgumentException ignored) {
                                System.out.println(Messenger.prefix(Messenger.format("Your current Teleportation sound does not exist on your Server Version! Please try setting a valid Sound Effect.")));
                            }
                        }
                        cancel();
                    }
                }.runTaskLater(plugin, delaytime * 20);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }

    private boolean isSoundsEnabled() {
        return Config.get().getBoolean("teleport.settings.sounds.enabled");
    }
}