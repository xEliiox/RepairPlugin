package xeliox.repairplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
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
                        int comparison = compareVersions(currentVersion, latestVersion);

                        if (comparison < 0) {
                            // Installed version is outdated
                            Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.getMessage() +
                                    ColorTranslator.translate("&cYour plugin is outdated! New version available: v" + latestVersion));
                            Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.getMessage() +
                                    ColorTranslator.translate("&fYou are currently using version: v" + currentVersion));
                        } else if (comparison > 0) {
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

    private static int compareVersions(@NotNull String version1, @NotNull String version2) {
        String[] v1Parts = version1.replaceAll("[^0-9.]", "").split("\\.");
        String[] v2Parts = version2.replaceAll("[^0-9.]", "").split("\\.");

        int maxLength = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < maxLength; i++) {
            int num1 = (i < v1Parts.length) ? Integer.parseInt(v1Parts[i]) : 0;
            int num2 = (i < v2Parts.length) ? Integer.parseInt(v2Parts[i]) : 0;

            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }
}
