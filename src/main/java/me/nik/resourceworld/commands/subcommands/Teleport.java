package me.nik.resourceworld.commands.subcommands;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.LocationFinder;
import me.nik.resourceworld.utils.TaskUtils;
import me.nik.resourceworld.utils.WorldUtils;
import net.milkbowl.vault.economy.EconomyResponse;
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
import java.util.Map;
import java.util.UUID;

public class Teleport extends SubCommand {

    private static boolean resettingWorld = false;
    private static boolean resettingNether = false;
    private static boolean resettingEnd = false;
    private final Map<UUID, Long> cooldown = new HashMap<>();

    private final LocationFinder locationFinder;

    public Teleport() {
        this.locationFinder = new LocationFinder();
    }

    public void setResettingWorld(boolean resettingWorld) {
        Teleport.resettingWorld = resettingWorld;
    }

    public void setResettingNether(boolean resettingNether) {
        Teleport.resettingNether = resettingNether;
    }

    public void setResettingEnd(boolean resettingEnd) {
        Teleport.resettingEnd = resettingEnd;
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
        return Permissions.TELEPORT.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            final Player player = (Player) sender;

            switch (args.length) {
                case 1:
                    if (WorldUtils.worldExists() && !resettingWorld) {
                        World worldResource = Bukkit.getWorld(Config.Setting.WORLD_NAME.getString());
                        teleport(player, worldResource);
                    } else {
                        player.sendMessage(MsgType.NOT_EXIST.getMessage());
                    }
                    break;
                case 2:
                    if (args[1].equalsIgnoreCase("nether")) {
                        if (!player.hasPermission(Permissions.TELEPORT_NETHER.getPermission())) {
                            player.sendMessage(MsgType.NO_PERMISSION.getMessage());
                            break;
                        }
                        if (WorldUtils.netherExists() && !resettingNether) {
                            World worldNether = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());
                            teleport(player, worldNether);
                        } else {
                            player.sendMessage(MsgType.NOT_EXIST.getMessage());
                        }
                        break;
                    } else if (args[1].equalsIgnoreCase("end")) {
                        if (!player.hasPermission(Permissions.TELEPORT_END.getPermission())) {
                            player.sendMessage(MsgType.NO_PERMISSION.getMessage());
                            return;
                        }
                        if (WorldUtils.endExists() && !resettingEnd) {
                            World worldEnd = Bukkit.getWorld(Config.Setting.END_NAME.getString());
                            teleport(player, worldEnd);
                        } else {
                            player.sendMessage(MsgType.NOT_EXIST.getMessage());
                        }
                        break;
                    }
                    break;
            }
        } else {
            switch (args.length) {
                case 2:
                    if (!WorldUtils.worldExists() || resettingWorld) {
                        sender.sendMessage(MsgType.NOT_EXIST.getMessage());
                        break;
                    }
                    World worldResource = Bukkit.getWorld(Config.Setting.WORLD_NAME.getString());
                    final String p = args[1];
                    try {
                        Player player = Bukkit.getPlayer(p);
                        teleport(player, worldResource);
                        sender.sendMessage(MsgType.TELEPORTING_PLAYER.getMessage().replaceAll("%player%", p).replaceAll("%world%", worldResource.getName()));
                    } catch (NullPointerException e) {
                        sender.sendMessage("Player not found.");
                    }
                    break;
                case 3:
                    final String p2 = args[2];
                    if (args[1].equalsIgnoreCase("nether")) {
                        if (!WorldUtils.netherExists() || resettingNether) {
                            sender.sendMessage(MsgType.NOT_EXIST.getMessage());
                            break;
                        }
                        World worldNether = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());
                        try {
                            Player player = Bukkit.getPlayer(p2);
                            teleport(player, worldNether);
                            sender.sendMessage(MsgType.TELEPORTING_PLAYER.getMessage().replaceAll("%player%", p2).replaceAll("%world%", worldNether.getName()));
                        } catch (NullPointerException e) {
                            sender.sendMessage("Player not found.");
                        }
                        break;
                    } else if (args[1].equalsIgnoreCase("end")) {
                        if (!WorldUtils.endExists() || resettingEnd) {
                            sender.sendMessage(MsgType.NOT_EXIST.getMessage());
                            break;
                        }
                        World worldEnd = Bukkit.getWorld(Config.Setting.END_NAME.getString());
                        try {
                            Player player = Bukkit.getPlayer(p2);
                            teleport(player, worldEnd);
                            sender.sendMessage(MsgType.TELEPORTING_PLAYER.getMessage().replaceAll("%player%", p2).replaceAll("%world%", worldEnd.getName()));
                        } catch (NullPointerException e) {
                            sender.sendMessage("Player not found.");
                        }
                        break;
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
        if (!dealWithCash(p)) return;
        if (Config.Setting.TELEPORT_DELAY.getInt() < 1) {
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(uuid, System.currentTimeMillis());
            }
            if (Config.Setting.TELEPORT_ASYNC.getBoolean()) {
                PaperLib.teleportAsync(p, locationFinder.generateLocation(world));
            } else {
                p.teleport(locationFinder.generateLocation(world));
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
                    PaperLib.teleportAsync(p, locationFinder.generateLocation(world));
                } else {
                    p.teleport(locationFinder.generateLocation(world));
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

    private boolean dealWithCash(Player player) {
        if (player.hasPermission("rw.admin")) return true;
        if (ResourceWorld.getEconomy() == null || Config.Setting.TELEPORT_PRICE.getDouble() < 1) return true;
        double price = Config.Setting.TELEPORT_PRICE.getDouble();
        EconomyResponse res = ResourceWorld.getEconomy().withdrawPlayer(player, price);
        if (res.transactionSuccess()) {
            player.sendMessage(MsgType.TELEPORT_PAID.getMessage().replace("%price%", ResourceWorld.getEconomy().format(res.amount)));
            return true;
        } else {
            player.sendMessage(MsgType.TELEPORT_ERROR.getMessage());
            return false;
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