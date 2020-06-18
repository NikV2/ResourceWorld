package me.nik.resourceworld.commands.subcommands;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.Messenger;
import me.nik.resourceworld.utils.TeleportUtils;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Teleport extends SubCommand {

    private final ResourceWorld plugin;

    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final int cdtime;
    final private int delaytime;
    final private PotionEffect effect;
    final private int volume;
    final private int pitch;
    private final boolean isSoundsEnabled;
    private final boolean paper;

    private boolean resettingWorld = false;
    private boolean resettingNether = false;
    private boolean resettingEnd = false;

    private final TeleportUtils teleportUtils;

    public Teleport(ResourceWorld plugin) {
        this.plugin = plugin;
        this.cdtime = plugin.getConfig().getInt("teleport.settings.cooldown");
        this.delaytime = plugin.getConfig().getInt("teleport.settings.delay");
        this.effect = new PotionEffect(PotionEffectType.getByName(plugin.getConfig().getString("teleport.settings.effects.effect")), plugin.getConfig().getInt("teleport.settings.effects.duration") * 20, plugin.getConfig().getInt("teleport.settings.effects.amplifier"));
        this.volume = plugin.getConfig().getInt("teleport.settings.sounds.volume");
        this.pitch = plugin.getConfig().getInt("teleport.settings.sounds.pitch");
        this.isSoundsEnabled = plugin.getConfig().getBoolean("teleport.settings.sounds.enabled");
        this.paper = plugin.getConfig().getBoolean("teleport.settings.async");
        this.teleportUtils = new TeleportUtils(plugin);
    }

    public void setResettingWorld(boolean resettingWorld) {
        this.resettingWorld = resettingWorld;
    }

    public void setResettingNether(boolean resettingNether) {
        this.resettingNether = resettingNether;
    }

    public void setResettingEnd(boolean resettingEnd) {
        this.resettingEnd = resettingEnd;
    }

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
    public String getPermission() {
        return "rw.tp";
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (!WorldUtils.worldExists() || resettingWorld) {
                    player.sendMessage(Messenger.message(MsgType.NOT_EXIST));
                    return;
                }
                World worldResource = Bukkit.getWorld(plugin.getConfig().getString("world.settings.world_name"));
                teleport(player, worldResource);
            } else if (args.length == 2 && args[1].equalsIgnoreCase("nether")) {
                if (!player.hasPermission("rw.tp.nether")) {
                    player.sendMessage(Messenger.message(MsgType.NO_PERMISSION));
                    return;
                }
                if (WorldUtils.netherExists() || !resettingNether) {
                    World worldNether = Bukkit.getWorld(plugin.getConfig().getString("nether_world.settings.world_name"));
                    teleport(player, worldNether);
                } else {
                    player.sendMessage(Messenger.message(MsgType.NOT_EXIST));
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("end")) {
                if (!player.hasPermission("rw.tp.end")) {
                    player.sendMessage(Messenger.message(MsgType.NO_PERMISSION));
                    return;
                }
                if (WorldUtils.endExists() || !resettingEnd) {
                    World worldEnd = Bukkit.getWorld(plugin.getConfig().getString("end_world.settings.world_name"));
                    teleport(player, worldEnd);
                } else {
                    player.sendMessage(Messenger.message(MsgType.NOT_EXIST));
                }
            }
        } else {
            if (args.length == 2) {
                if (!WorldUtils.worldExists() || resettingWorld) {
                    sender.sendMessage(Messenger.message(MsgType.NOT_EXIST));
                    return;
                }
                World worldResource = Bukkit.getWorld(plugin.getConfig().getString("world.settings.world_name"));
                String p = args[1];
                try {
                    Player player = Bukkit.getPlayer(p);
                    teleport(player, worldResource);
                    sender.sendMessage(Messenger.message(MsgType.TELEPORTING_PLAYER).replaceAll("%player%", p).replaceAll("%world%", worldResource.getName()));
                } catch (NullPointerException e) {
                    sender.sendMessage("Player not found.");
                }
            } else if (args.length == 3 && args[1].equalsIgnoreCase("nether")) {
                if (!WorldUtils.netherExists() || resettingNether) {
                    sender.sendMessage(Messenger.message(MsgType.NOT_EXIST));
                    return;
                }
                World worldNether = Bukkit.getWorld(plugin.getConfig().getString("nether_world.settings.world_name"));
                String p = args[2];
                try {
                    Player player = Bukkit.getPlayer(p);
                    teleport(player, worldNether);
                    sender.sendMessage(Messenger.message(MsgType.TELEPORTING_PLAYER).replaceAll("%player%", p).replaceAll("%world%", worldNether.getName()));
                } catch (NullPointerException e) {
                    sender.sendMessage("Player not found.");
                }
            } else if (args.length == 3 && args[1].equalsIgnoreCase("end")) {
                if (!WorldUtils.endExists() || resettingEnd) {
                    sender.sendMessage(Messenger.message(MsgType.NOT_EXIST));
                    return;
                }
                World worldEnd = Bukkit.getWorld(plugin.getConfig().getString("end_world.settings.world_name"));
                String p = args[2];
                try {
                    Player player = Bukkit.getPlayer(p);
                    teleport(player, worldEnd);
                    sender.sendMessage(Messenger.message(MsgType.TELEPORTING_PLAYER).replaceAll("%player%", p).replaceAll("%world%", worldEnd.getName()));
                } catch (NullPointerException e) {
                    sender.sendMessage("Player not found.");
                }
            }
        }
    }

    private void teleport(Player p, World world) {
        UUID uuid = p.getUniqueId();
        if (cooldown.containsKey(uuid)) {
            long secondsleft = ((cooldown.get(uuid) / 1000) + cdtime) - (System.currentTimeMillis() / 1000);
            if (secondsleft > 0) {
                p.sendMessage(Messenger.message(MsgType.COOLDOWN_MESSAGE).replaceAll("%seconds%", String.valueOf(secondsleft)));
                return;
            }
            cooldown.remove(uuid);
        }
        if (delaytime < 1) {
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(uuid, System.currentTimeMillis());
            }
            if (paper) {
                PaperLib.teleportAsync(p, teleportUtils.generateLocation(world));
            } else {
                p.teleport(teleportUtils.generateLocation(world));
            }
            p.addPotionEffect(effect);
            if (isSoundsEnabled) {
                try {
                    p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("teleport.settings.sounds.sound")), volume, pitch);
                } catch (IllegalArgumentException ignored) {
                }
            }
        } else {
            p.sendMessage(Messenger.message(MsgType.TELEPORT_DELAY).replaceAll("%seconds%", String.valueOf(delaytime)));
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(uuid, System.currentTimeMillis());
            }
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (paper) {
                        PaperLib.teleportAsync(p, teleportUtils.generateLocation(world));
                    } else {
                        p.teleport(teleportUtils.generateLocation(world));
                    }
                    p.addPotionEffect(effect);
                    if (isSoundsEnabled) {
                        try {
                            p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("teleport.settings.sounds.sound")), volume, pitch);
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            }.runTaskLater(plugin, delaytime * 20);
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        if (args.length == 2) {
            List<String> worlds = new ArrayList<>();
            if (WorldUtils.netherExists()) {
                worlds.add("nether");
            }
            if (WorldUtils.endExists()) {
                worlds.add("end");
            }
            return worlds;
        }

        return null;
    }
}