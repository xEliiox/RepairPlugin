package xeliox.repairplugin.utils;

import org.bukkit.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTranslator {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String translate(String message) {
        ChatColor.translateAlternateColorCodes('&', message);

        if (ServerUtils.VersionIsNew()) {
            Matcher matcher = HEX_PATTERN.matcher(message);
            StringBuffer buffer = new StringBuffer();

            while (matcher.find()) {
                String hexColor = matcher.group();
                matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(hexColor).toString());
            }
            matcher.appendTail(buffer);
            message = buffer.toString();

        }
        return message;
    }
}
