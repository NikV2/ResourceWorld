package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.api.Manager;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.files.Lang;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Teleport extends SubCommand {
    private final HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private final int cdtime = Config.get().getInt("teleport.settings.cooldown");
    final private int delaytime = Config.get().getInt("teleport.settings.delay");
    final private PotionEffect effect = new PotionEffect(PotionEffectType.getByName(Config.get().getString("teleport.settings.effects.effect")), Config.get().getInt("teleport.settings.effects.duration") * 20, Config.get().getInt("teleport.settings.effects.amplifier"));
    final private int volume = Config.get().getInt("teleport.settings.sounds.volume");
    final private int pitch = Config.get().getInt("teleport.settings.sounds.pitch");

    private final Manager manager = new Manager();

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
        return "/resource tp <nether, end>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("rw.tp")) {
            player.sendMessage(Messenger.message("no_perm"));
            return;
        }
        if (args.length == 1) {
            if (!manager.worldExists()) {
                player.sendMessage(Messenger.message("not_exist"));
                return;
            }
            World worldResource = Bukkit.getWorld(Config.get().getString("world.settings.world_name"));
            teleport(player, worldResource);
        } else if (args.length == 2 && args[1].equalsIgnoreCase("nether")) {
            if (Config.get().getBoolean("nether_world.settings.enabled") && manager.netherExists()) {
                World worldNether = Bukkit.getWorld(Config.get().getString("nether_world.settings.world_name"));
                teleport(player, worldNether);
            } else {
                player.sendMessage(Messenger.message("not_exist"));
            }
        } else if (args.length == 2 && args[1].equalsIgnoreCase("end")) {
            if (Config.get().getBoolean("end_world.settings.enabled") && manager.endExists()) {
                World worldEnd = Bukkit.getWorld(Config.get().getString("end_world.settings.world_name"));
                teleport(player, worldEnd);
            } else {
                player.sendMessage(Messenger.message("not_exist"));
            }
        }
    }

    private void teleport(Player p, World world) {
        if (cooldown.containsKey(p.getUniqueId())) {
            long secondsleft = ((cooldown.get(p.getUniqueId()) / 1000) + cdtime) - (System.currentTimeMillis() / 1000);
            if (secondsleft > 0) {
                p.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("cooldown_message").replaceAll("%seconds%", String.valueOf(secondsleft)))));
                return;
            } else return;
        }
        if (Config.get().getInt("teleport.settings.delay") < 1) {
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(p.getUniqueId(), System.currentTimeMillis());
            }
            p.teleport(TeleportUtils.generateLocation(world));
            p.addPotionEffect(effect);
            if (isSoundsEnabled()) {
                try {
                    p.playSound(p.getLocation(), Sound.valueOf(Config.get().getString("teleport.settings.sounds.sound")), volume, pitch);
                } catch (IllegalArgumentException ignored) {
                    System.out.println(Messenger.prefix(Messenger.format("Your current Teleportation sound does not exist on your Server Version! Please try setting a valid Sound Effect.")));
                }
            }
        } else {
            p.sendMessage(Messenger.prefix(Messenger.format(Lang.get().getString("teleport_delay").replaceAll("%seconds%", String.valueOf(delaytime)))));
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(p.getUniqueId(), System.currentTimeMillis());
            }
            new BukkitRunnable() {

                @Override
                public void run() {
                    p.teleport(TeleportUtils.generateLocation(world));
                    p.addPotionEffect(effect);
                    if (isSoundsEnabled()) {
                        try {
                            p.playSound(p.getLocation(), Sound.valueOf(Config.get().getString("teleport.settings.sounds.sound")), volume, pitch);
                        } catch (IllegalArgumentException ignored) {
                            System.out.println(Messenger.prefix(Messenger.format("Your current Teleportation sound does not exist on your Server Version! Please try setting a valid Sound Effect.")));
                        }
                    }
                    cancel();
                }
            }.runTaskLater(plugin, delaytime * 20);
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {

        if (args.length == 2) {
            List<String> worlds = new ArrayList<>();
            if (manager.netherExists()) {
                worlds.add("nether");
            }
            if (manager.endExists()) {
                worlds.add("end");
            }
            return worlds;
        }

        return null;
    }

    private boolean isSoundsEnabled() {
        return Config.get().getBoolean("teleport.settings.sounds.enabled");
    }
}