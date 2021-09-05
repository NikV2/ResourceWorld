package me.nik.resourceworld.commands.subcommands;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.LocationFinder;
import me.nik.resourceworld.utils.TaskUtils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Teleport extends SubCommand {

    private final Map<UUID, Long> cooldown = new HashMap<>();

    private final LocationFinder locationFinder;

    public Teleport() {
        this.locationFinder = new LocationFinder();
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
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            switch (args.length) {
                case 2:
                    player = Bukkit.getPlayer(args[1]);
                    break;
                case 3:
                    player = Bukkit.getPlayer(args[2]);
                    break;
            }
        }

        if (player == null) {
            sender.sendMessage("Player not found.");
            return;
        }
        switch (args.length) {
            case 1:
                teleport(player, Bukkit.getWorld(Config.Setting.WORLD_NAME.getString()));
                break;
            case 2:
                switch (args[1].toLowerCase()) {
                    case "nether":
                        if (!player.hasPermission(Permissions.TELEPORT_NETHER.getPermission())) {
                            player.sendMessage(MsgType.NO_PERMISSION.getMessage());
                            return;
                        }

                        teleport(player, Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()));
                        break;
                    case "end":
                        if (!player.hasPermission(Permissions.TELEPORT_END.getPermission())) {
                            player.sendMessage(MsgType.NO_PERMISSION.getMessage());
                            return;
                        }

                        teleport(player, Bukkit.getWorld(Config.Setting.END_NAME.getString()));
                        break;
                }
                break;
        }
    }

    /**
     * This is ugly, but i'm lazy to re-code this
     */
    private void teleport(Player p, World world) {
        final UUID uuid = p.getUniqueId();
        if (cooldown.containsKey(uuid)) {
            long secondsleft = ((cooldown.get(uuid) / 1000L) + Config.Setting.TELEPORT_COOLDOWN.getInt()) - (System.currentTimeMillis() / 1000L);
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
            doTeleport(p, world);
        } else {
            p.sendMessage(MsgType.TELEPORT_DELAY.getMessage().replaceAll("%seconds%", String.valueOf(Config.Setting.TELEPORT_DELAY.getInt())));
            if (!p.hasPermission("rw.admin")) {
                cooldown.put(uuid, System.currentTimeMillis());
            }
            TaskUtils.taskLater(() -> doTeleport(p, world), Config.Setting.TELEPORT_DELAY.getLong() * 20L);
        }
    }

    private void doTeleport(Player p, World world) {
        Location target = locationFinder.generateLocation(world);
        PlayerTeleportEvent event = new PlayerTeleportEvent(p, p.getLocation(), target, PlayerTeleportEvent.TeleportCause.COMMAND);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        if (Config.Setting.TELEPORT_ASYNC.getBoolean()) {
            PaperLib.teleportAsync(p, target);
        } else {
            p.teleport(target);
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Config.Setting.TELEPORT_EFFECT.getString()), Config.Setting.TELEPORT_EFFECT_DURATION.getInt() * 20, Config.Setting.TELEPORT_EFFECT_AMPLIFIER.getInt()));
        if (Config.Setting.TELEPORT_SOUND_ENABLED.getBoolean()) {
            try {
                p.playSound(p.getLocation(), Sound.valueOf(Config.Setting.TELEPORT_SOUND.getString()), 2, 2);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private boolean dealWithCash(Player player) {
        if (player.hasPermission(Permissions.ADMIN.getPermission())) return true;
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
            if (Bukkit.getWorld(Config.Setting.NETHER_NAME.getString()) != null) {
                worlds.add("nether");
            }
            if (Bukkit.getWorld(Config.Setting.END_NAME.getString()) != null) {
                worlds.add("end");
            }
            return worlds;
        }

        return null;
    }
}