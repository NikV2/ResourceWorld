package me.nik.resourceworld.utils;

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

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days".
     */
    public static String getDays(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        if (days != 0) {
            return days + " Days";
        }
        return "";
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Hours".
     */
    public static String getHours(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours != 0) {
            return hours + " Hours";
        }
        return "";
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Minutes".
     */
    public static String getMinutes(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes != 0) {
            return minutes + " Minutes";
        }
        return "";
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Seconds".
     */
    public static String getSeconds(long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds != 0) {
            return seconds + " Seconds";
        }
        return "";
    }
}