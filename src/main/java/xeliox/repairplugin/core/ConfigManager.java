package xeliox.repairplugin.core;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;
import xeliox.repairplugin.RepairPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfigManager {
    private YamlFile config;
    private boolean anvilInteraction;
    private boolean eventAnvilInteraction;
    private int experienceCost;
    private int experienceCostAll;
    private int anvilExperienceCost;

    public ConfigManager(@NotNull RepairPlugin plugin, Logger logger) throws IOException {
        File dataFolder = plugin.getDataFolder();
        File configFile = new File(dataFolder, "config.yml");

        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            logger.severe("Failed to create plugin folder!");
            return;
        }

        if (!configFile.exists() || configFile.length() == 0) {
            plugin.saveResource("config.yml", false);
        }

        config = new YamlFile(configFile);

        try {
            config.load();
        } catch (Exception e) {
            logger.severe("Failed to load config.yml: " + e.getMessage());
        }

        loadConfiguration(config);
    }

    public void loadConfiguration(YamlFile config) throws IOException {

        boolean[] saveRequired = new boolean[]{false};

        anvilInteraction = getOrSetDefault("Settings.disableAnvilInteraction", false, saveRequired);
        eventAnvilInteraction = getOrSetDefault("Settings.cancelEventAnvilInteraction", true,saveRequired);
        experienceCost = getIntOrDefault("Settings.experience_cost", 10,saveRequired);
        experienceCostAll = getIntOrDefault("Settings.experience_cost_all", 15,saveRequired);
        anvilExperienceCost = getIntOrDefault("Settings.anvil_experience_cost", 10,saveRequired);

        for (Messages message : Messages.values()) {
            if (!config.contains(message.path)) {
                config.set(message.path, message.defaultMessage);
                saveRequired[0] = true;
            }
            message.loadMessage(config);
        }

        if (saveRequired[0]) {
            config.save();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrSetDefault(String path, T defaultValue, boolean[] saveFlag) {
        if (!config.contains(path)) {
            config.set(path, defaultValue);
            saveFlag[0] = true;
            return defaultValue;
        }
        return (T) config.get(path);
    }


    private int getIntOrDefault(String path, int defaultValue, boolean[] saveFlag) {
        if (!config.contains(path)) {
            config.set(path, defaultValue);
            saveFlag[0] = true;
            return defaultValue;
        }
        Object value = config.get(path);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }


    public boolean isDisableAnvilInteraction() {
        return anvilInteraction;
    }

    public boolean isEventAnvilInteraction() {
        return eventAnvilInteraction;
    }

    public int getExperienceCost() {
        return experienceCost;
    }

    public int getExperienceCostAll() {
        return experienceCostAll;
    }

    public int getAnvilExperienceCost() {
        return anvilExperienceCost;
    }

    public void reloadConfig() throws IOException {
        try {
            this.config.load();
            loadConfiguration(this.config);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error reloading configuration: " + e.getMessage());
            throw new IOException("Could not reload configuration", e);
        }
    }
}

