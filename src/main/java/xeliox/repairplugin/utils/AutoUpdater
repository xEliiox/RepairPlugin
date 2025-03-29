package xeliox.repairplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xeliox.repairplugin.core.Messages;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class AutoUpdater {

    private static final String RESOURCE_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
    private static final int RESOURCE_ID = 120518;

    public static void checkForUpdates(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(RESOURCE_URL + RESOURCE_ID);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String latestVersion = reader.readLine();
                    reader.close();

                    if (latestVersion != null) {
                        String currentVersion = plugin.getDescription().getVersion();
                        // Convert the version strings to integers for comparison (e.g., "1.0.5" -> 105)
                        int currentVersionInt = convertVersionToInt(currentVersion);
                        int latestVersionInt = convertVersionToInt(latestVersion);

                        if (currentVersionInt < latestVersionInt) {
                            // Installed version is outdated
                            Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.getMessage() +
                                    ColorTranslator.translate("&cYour plugin is outdated! New version available: v" + latestVersion));
                            Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.getMessage() +
                                    ColorTranslator.translate("&fYou are currently using version: v" + currentVersion));
                        } else if (currentVersionInt > latestVersionInt) {
                            // Installed version is newer (experimental/tested version)
                            Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.getMessage() +
                                    ColorTranslator.translate("&eYou are using a test version v" + currentVersion));
                        } else {
                            // Installed version is the latest
                            Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.getMessage() +
                                    ColorTranslator.translate("&aYou are using the latest version v" + currentVersion));
                        }
                    }
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.getMessage() +
                            ColorTranslator.translate("&cFailed to check for updates: " + e.getMessage()));
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private static int convertVersionToInt(String version) {
        version = version.replaceAll("[^0-9.]", "");
        String[] parts = version.split("\\.");
        int major = (parts.length > 0) ? Integer.parseInt(parts[0]) * 10000 : 0;
        int minor = (parts.length > 1) ? Integer.parseInt(parts[1]) * 100 : 0;
        int patch = (parts.length > 2) ? Integer.parseInt(parts[2]) : 0;
        return major + minor + patch;
    }
}
