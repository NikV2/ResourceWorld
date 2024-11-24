package me.nik.resourceworld.commands.subcommands;

import me.nik.resourceworld.commands.SubCommand;
import me.nik.resourceworld.files.Config;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.Permissions;
import me.nik.resourceworld.utils.LocationFinder;
import me.nik.resourceworld.utils.TaskUtils;
import me.nik.resourceworld.utils.custom.ExpiringMap;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teleport extends SubCommand {

    private final Economy economy;
    /*
    Clear outdated cache automatically.
     */
    private final ExpiringMap<UUID, Long> cooldown = new ExpiringMap<>(600000);
    private final LocationFinder locationFinder = new LocationFinder();

    public Teleport() {

        //Load Vault if found
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {

            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

            if (rsp != null) {
                this.economy = rsp.getProvider();
                return;
            }
        }

        this.economy = null;
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
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        UUID uuid = player.getUniqueId();

        long currentTime = System.currentTimeMillis();

        if (this.cooldown.containsKey(uuid)) {

            long secondsleft = ((this.cooldown.get(uuid) / 1000L) + Config.Setting.TELEPORT_COOLDOWN.getLong()) - (currentTime / 1000L);

            if (secondsleft <= 0L) {

                this.cooldown.remove(uuid);

            } else {

                player.sendMessage(MsgType.COOLDOWN_MESSAGE.getMessage().replaceAll("%seconds%", String.valueOf(secondsleft)));

                return;
            }
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

        boolean notBypassing = !player.hasPermission(Permissions.ADMIN.getPermission());

        //Handle vault
        if (this.economy != null && Config.Setting.TELEPORT_PRICE.getDouble() > 0D && notBypassing) {

            EconomyResponse res = this.economy.withdrawPlayer(player, Config.Setting.TELEPORT_PRICE.getDouble());

            if (res.transactionSuccess()) {

                player.sendMessage(MsgType.TELEPORT_PAID.getMessage().replace("%price%", this.economy.format(res.amount)));

            } else {

                player.sendMessage(MsgType.TELEPORT_ERROR.getMessage());

                return;
            }
        }

        //Add to cooldown
        if (notBypassing && Config.Setting.TELEPORT_COOLDOWN.getLong() > 0L) {

            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis());
        }

        //Finally teleport
        if (Config.Setting.TELEPORT_DELAY.getLong() <= 0L) {

            this.locationFinder.teleportSafely(player, world);

        } else {

            player.sendMessage(MsgType.TELEPORT_DELAY.getMessage().replaceAll("%seconds%", String.valueOf(Config.Setting.TELEPORT_DELAY.getLong())));

            World finalWorld = world;
            TaskUtils.taskLater(() -> this.locationFinder.teleportSafely(player, finalWorld), Config.Setting.TELEPORT_DELAY.getLong() * 20L);
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