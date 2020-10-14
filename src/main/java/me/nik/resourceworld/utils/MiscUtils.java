package me.nik.resourceworld.utils;

import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class MiscUtils {

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days != 0) {
            sb.append(days);
            sb.append(" Days ");
        }
        if (hours != 0) {
            sb.append(hours);
            sb.append(" Hours ");
        }
        if (minutes != 0) {
            sb.append(minutes);
            sb.append(" Minutes ");
        }
        if (seconds != 0) {
            sb.append(seconds);
            sb.append(" Seconds");
        }

        return (sb.toString());
    }

    public static boolean isLegacy() {
        return Bukkit.getVersion().contains("1.8")
                || Bukkit.getVersion().contains("1.9")
                || Bukkit.getVersion().contains("1.10")
                || Bukkit.getVersion().contains("1.11")
                || Bukkit.getVersion().contains("1.12");
    }
}