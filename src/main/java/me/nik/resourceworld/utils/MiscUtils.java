package me.nik.resourceworld.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.TimeUnit;

public class MiscUtils {

    private MiscUtils() {
    }

    private static final boolean legacy = Bukkit.getVersion().contains("1.8")
            || Bukkit.getVersion().contains("1.9")
            || Bukkit.getVersion().contains("1.10")
            || Bukkit.getVersion().contains("1.11")
            || Bukkit.getVersion().contains("1.12");

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis) {
        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(days);
            sb.append(" Days ");
        }
        if (hours > 0) {
            sb.append(hours);
            sb.append(" Hours ");
        }
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(" Minutes ");
        }
        if (seconds > 0) {
            sb.append(seconds);
            sb.append(" Seconds");
        }

        return (sb.toString());
    }

    /**
     * Convert a location to a string seperated by a comma
     * So you can later grab it and convert it to a location by splitting the comma
     *
     * @param location The location
     * @return The location as a string
     */
    public static String locationToString(final Location location) {
        return location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getWorld().getName();
    }

    /**
     * Convert a string to a location
     *
     * @param location The location as a string
     * @return The bukkit location
     */
    public static Location stringToLocation(final String location) {
        final String[] data = location.split(",");

        try {
            final double x = Double.parseDouble(data[0]);
            final double y = Double.parseDouble(data[1]);
            final double z = Double.parseDouble(data[2]);
            final World world = Bukkit.getWorld(data[3]);

            return new Location(world, x, y, z);
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isLegacy() {
        return legacy;
    }
}