package me.nik.resourceworld.utils;

import me.nik.resourceworld.ResourceWorld;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatUtils {

    private static final char SECTION = '\u00a7';
    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)&#([0-9a-f]{6})");
    private static final Pattern TAG_HEX_PATTERN = Pattern.compile("(?i)<#([0-9a-f]{6})>");
    private static final Pattern STRIP_PATTERN = Pattern.compile("(?i)&(#[0-9a-f]{6}|[0-9a-fk-orx])");
    private static final String LEGACY_CODES = "0123456789abcdef";
    private static final int[] LEGACY_COLORS = {
            0x000000, 0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA,
            0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF
    };
    private static final boolean HEX_SUPPORTED = detectHex();

    private ChatUtils() {
    }

    private static boolean detectHex() {

        try {
            Class.forName("net.md_5.bungee.api.ChatColor").getMethod("of", String.class);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static String format(final String msg) {

        if (msg == null || msg.isEmpty()) return "";

        if (msg.indexOf('&') == -1 && msg.indexOf('<') == -1) return msg;

        return legacy(msg);
    }

    private static String legacy(String msg) {

        String result = msg;

        if (result.indexOf("<#") != -1) result = replaceHex(TAG_HEX_PATTERN, result);

        if (result.indexOf("&#") != -1) result = replaceHex(HEX_PATTERN, result);

        return ChatColor.translateAlternateColorCodes('&', result);
    }

    private static String replaceHex(Pattern pattern, String input) {

        Matcher matcher = pattern.matcher(input);

        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) matcher.appendReplacement(buffer, Matcher.quoteReplacement(hex(matcher.group(1))));

        matcher.appendTail(buffer);

        return buffer.toString();
    }

    private static String hex(String code) {

        if (!HEX_SUPPORTED) return String.valueOf(SECTION) + nearest(Integer.parseInt(code, 16));

        StringBuilder builder = new StringBuilder().append(SECTION).append('x');

        for (int i = 0; i < code.length(); i++) builder.append(SECTION).append(Character.toLowerCase(code.charAt(i)));

        return builder.toString();
    }

    private static char nearest(int rgb) {

        int best = 0;
        long bestDistance = Long.MAX_VALUE;

        for (int i = 0; i < LEGACY_COLORS.length; i++) {

            long r = ((rgb >> 16) & 0xFF) - ((LEGACY_COLORS[i] >> 16) & 0xFF);
            long g = ((rgb >> 8) & 0xFF) - ((LEGACY_COLORS[i] >> 8) & 0xFF);
            long b = (rgb & 0xFF) - (LEGACY_COLORS[i] & 0xFF);

            long distance = r * r + g * g + b * b;

            if (distance < bestDistance) {
                bestDistance = distance;
                best = i;
            }
        }

        return LEGACY_CODES.charAt(best);
    }

    public static String stripColorCodes(final String input) {
        return ChatColor.stripColor(STRIP_PATTERN.matcher(input).replaceAll(""));
    }

    public static String plain(final String input) {

        StringBuilder builder = new StringBuilder(input.length());

        for (int i = 0; i < input.length(); i++) {

            char c = input.charAt(i);

            if ((c == '&' || c == SECTION) && i + 1 < input.length()) {

                char next = input.charAt(i + 1);

                if (c == '&' && next == '#' && isHex(input, i + 2, 6)) {
                    i += 7;
                    continue;
                }

                if ("0123456789abcdefklmnorxABCDEFKLMNORX".indexOf(next) != -1) {
                    i++;
                    continue;
                }
            }

            builder.append(c);
        }

        return builder.toString().trim();
    }

    private static boolean isHex(String input, int start, int length) {

        if (start + length > input.length()) return false;

        for (int i = start; i < start + length; i++) {

            if (Character.digit(input.charAt(i), 16) == -1) return false;
        }

        return true;
    }

    public static void log(final String... messages) {
        for (String msg : messages) {
            if (msg != null) {
                ResourceWorld.getInstance().getLogger().info(msg);
            }
        }
    }
}