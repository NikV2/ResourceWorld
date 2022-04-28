package me.nik.resourceworld.commands.subcommands;

import io.papermc.lib.PaperLib;
import me.nik.resourceworld.Permissions;
import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.utils.LocationFinder;
import me.nik.resourceworld.utils.TaskUtils;
import me.nik.resourceworld.utils.custom.ExpiringMap;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teleport extends SubCommand {

    /*
    Clear outdated cache automatically.
     */
    private final ExpiringMap<UUID, Long> cooldown = new ExpiringMap<>(600000);

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

        final Player player;

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

                default:

                    player = null;

                    break;
            }
        }

        if (player == null) {

            sender.sendMessage("Player not found.");

            return;
        }

        World world = null;

        switch (args.length) {

            case 1: {

                world = Bukkit.getWorld(Config.Setting.WORLD_NAME.getString());

                break;
            }

            case 2: {

                switch (args[1].toLowerCase()) {

                    case "nether":

                        if (!player.hasPermission(Permissions.TELEPORT_NETHER.getPermission())) {

                            player.sendMessage(MsgType.NO_PERMISSION.getMessage());

                            return;
                        }

                        world = Bukkit.getWorld(Config.Setting.NETHER_NAME.getString());

                        break;

                    case "end":

                        if (!player.hasPermission(Permissions.TELEPORT_END.getPermission())) {

                            player.sendMessage(MsgType.NO_PERMISSION.getMessage());

                            return;
                        }

                        world = Bukkit.getWorld(Config.Setting.END_NAME.getString());

                        break;
                }
            }
        }

        if (world == null) return;

        final UUID uuid = player.getUniqueId();

        if (this.cooldown.containsKey(uuid)) {

            long secondsleft = ((this.cooldown.get(uuid) / 1000L) + Config.Setting.TELEPORT_COOLDOWN.getInt()) - (System.currentTimeMillis() / 1000L);

            if (secondsleft > 0) {

                player.sendMessage(MsgType.COOLDOWN_MESSAGE.getMessage().replaceAll("%seconds%", String.valueOf(secondsleft)));

                return;
            }

            this.cooldown.remove(uuid);
        }

        if (!dealWithCash(player)) return;

        if (!player.hasPermission("rw.admin")) cooldown.put(uuid, System.currentTimeMillis());

        if (Config.Setting.TELEPORT_DELAY.getInt() < 1) {

            teleport(player, new LocationFinder().generateLocation(world));

        } else {

            player.sendMessage(MsgType.TELEPORT_DELAY.getMessage().replaceAll("%seconds%", String.valueOf(Config.Setting.TELEPORT_DELAY.getInt())));

            World finalWorld = world;
            TaskUtils.taskLater(() -> teleport(player, new LocationFinder().generateLocation(finalWorld)), Config.Setting.TELEPORT_DELAY.getLong() * 20L);
        }
    }

    private void teleport(Player player, Location location) {

        if (!player.hasPermission("rw.admin")) cooldown.put(player.getUniqueId(), System.currentTimeMillis());

        PaperLib.teleportAsync(player, location);

        player.addPotionEffect(
                new PotionEffect(
                        PotionEffectType.getByName(Config.Setting.TELEPORT_EFFECT.getString()),
                        Config.Setting.TELEPORT_EFFECT_DURATION.getInt() * 20,
                        Config.Setting.TELEPORT_EFFECT_AMPLIFIER.getInt()
                )
        );

        if (Config.Setting.TELEPORT_SOUND_ENABLED.getBoolean()) {

            try {
                player.playSound(player.getLocation(), Sound.valueOf(Config.Setting.TELEPORT_SOUND.getString()), 2, 2);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private boolean dealWithCash(Player player) {
        if (player.hasPermission(Permissions.ADMIN.getPermission())
                || ResourceWorld.getEconomy() == null
                || Config.Setting.TELEPORT_PRICE.getDouble() < 1) return true;

        EconomyResponse res = ResourceWorld.getEconomy().withdrawPlayer(player, Config.Setting.TELEPORT_PRICE.getDouble());

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