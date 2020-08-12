package me.nik.resourceworld.commands.subcommands;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.TaskUtils;
import me.nik.resourceworld.utils.TeleportUtils;
import me.nik.resourceworld.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Teleport extends SubCommand {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    private boolean resettingWorld = false;
    private boolean resettingNether = false;
    private boolean resettingEnd = false;

    private final TeleportUtils teleportUtils;

    public Teleport() {
        this.teleportUtils = new TeleportUtils();
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
                    player.sendMessage(MsgType.NOT_EXIST.getMessage());
                    return;
                }
                World worldResource = Bukkit.getWorld(Config.Setting.WORLD_NAME.getString());
                teleport(player, worldResource);
            } else if (args.length == 2 && args[1].equalsIgnoreCase("nether")) {
                if (!player.hasPermission("rw.tp.nether")) {
                    player.sendMessage(MsgType.NO_PERMISSION.getMessage());
                    return;
                }
                if (WorldUtils.netherExists() || !resettingNether) {
                    World worldNether = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());
                    teleport(player, worldNether);
                } else {
                    player.sendMessage(MsgType.NOT_EXIST.getMessage());
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("end")) {
                if (!player.hasPermission("rw.tp.end")) {
                    player.sendMessage(MsgType.NO_PERMISSION.getMessage());
                    return;
                }
                if (WorldUtils.endExists() || !resettingEnd) {
                    World worldEnd = Bukkit.getWorld(Config.Setting.END_NAME.getString());
                    teleport(player, worldEnd);
                } else {
                    player.sendMessage(MsgType.NOT_EXIST.getMessage());
                }
            }
        } else {
            if (args.length == 2) {
                if (!WorldUtils.worldExists() || resettingWorld) {
                    sender.sendMessage(MsgType.NOT_EXIST.getMessage());
                    return;
                }
                World worldResource = Bukkit.getWorld(Config.Setting.WORLD_NAME.getString());
                String p = args[1];
                try {
                    Player player = Bukkit.getPlayer(p);
                    teleport(player, worldResource);
                    sender.sendMessage(MsgType.TELEPORTING_PLAYER.getMessage().replaceAll("%player%", p).replaceAll("%world%", worldResource.getName()));
                } catch (NullPointerException e) {
                    sender.sendMessage("Player not found.");
                }
            } else if (args.length == 3 && args[1].equalsIgnoreCase("nether")) {
                if (!WorldUtils.netherExists() || resettingNether) {
                    sender.sendMessage(MsgType.NOT_EXIST.getMessage());
                    return;
                }
                World worldNether = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());
                String p = args[2];
                try {
                    Player player = Bukkit.getPlayer(p);
                    teleport(player, worldNether);
                    sender.sendMessage(MsgType.TELEPORTING_PLAYER.getMessage().replaceAll("%player%", p).replaceAll("%world%", worldNether.getName()));
                } catch (NullPointerException e) {
                    sender.sendMessage("Player not found.");
                }
            } else if (args.length == 3 && args[1].equalsIgnoreCase("end")) {
                if (!WorldUtils.endExists() || resettingEnd) {
                    sender.sendMessage(MsgType.NOT_EXIST.getMessage());
                    return;
                }
                World worldEnd = Bukkit.getWorld(Config.Setting.END_NAME.getString());
                String p = args[2];
                try {
                    Player player = Bukkit.getPlayer(p);
                    teleport(player, worldEnd);
                    sender.sendMessage(MsgType.TELEPORTING_PLAYER.getMessage().replaceAll("%player%", p).replaceAll("%world%", worldEnd.getName()));
                } catch (NullPointerException e) {
                    sender.sendMessage("Player not found.");
                }
            }
        }
    }

    private void teleport(Player p, World world) {
        UUID uuid = p.getUniqueId();
        if (cooldown.containsKey(uuid)) {
            long secondsleft = ((cooldown.get(uuid) / 1000) + Config.Setting.TELEPORT_COOLDOWN.getInt()) - (System.currentTimeMillis() / 1000);
            if (secondsleft > 0) {
                p.sendMessage(MsgType.COOLDOWN_MESSAGE.getMessage().replaceAll("%seconds%", String.valueOf(secondsleft)));
                return;
            }
            cooldown.remove(uuid);
        }
        if (Config.Setting.TELEPORT_DELAY.getInt() < 1) {
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(uuid, System.currentTimeMillis());
            }
            if (Config.Setting.TELEPORT_ASYNC.getBoolean()) {
                PaperLib.teleportAsync(p, teleportUtils.generateLocation(world));
            } else {
                p.teleport(teleportUtils.generateLocation(world));
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Config.Setting.TELEPORT_EFFECT.getString()), Config.Setting.TELEPORT_EFFECT_DURATION.getInt() * 20, Config.Setting.TELEPORT_EFFECT_AMPLIFIER.getInt()));
            if (Config.Setting.TELEPORT_SOUND_ENABLED.getBoolean()) {
                try {
                    p.playSound(p.getLocation(), Sound.valueOf(Config.Setting.TELEPORT_SOUND.getString()), 2, 2);
                } catch (IllegalArgumentException ignored) {
                }
            }
        } else {
            p.sendMessage(MsgType.TELEPORT_DELAY.getMessage().replaceAll("%seconds%", String.valueOf(Config.Setting.TELEPORT_DELAY.getInt())));
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(uuid, System.currentTimeMillis());
            }
            TaskUtils.taskLater(() -> {
                if (Config.Setting.TELEPORT_ASYNC.getBoolean()) {
                    PaperLib.teleportAsync(p, teleportUtils.generateLocation(world));
                } else {
                    p.teleport(teleportUtils.generateLocation(world));
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Config.Setting.TELEPORT_EFFECT.getString()), Config.Setting.TELEPORT_EFFECT_DURATION.getInt() * 20, Config.Setting.TELEPORT_EFFECT_AMPLIFIER.getInt()));
                if (Config.Setting.TELEPORT_SOUND_ENABLED.getBoolean()) {
                    try {
                        p.playSound(p.getLocation(), Sound.valueOf(Config.Setting.TELEPORT_SOUND.getString()), 2, 2);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }, Config.Setting.TELEPORT_DELAY.getInt() * 20);
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