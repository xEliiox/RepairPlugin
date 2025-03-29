package xeliox.repairplugin.core;

import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;
import xeliox.repairplugin.RepairPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfigManager {
    private YamlFile config;
    private boolean anvilInteraction;
    private int experienceCost;
    private int experienceCostAll;

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

        loadConfiguration();
    }

    public void loadConfiguration() throws IOException {

        anvilInteraction = getOrSetDefault("Settings.disableAnvilInteraction", false);
        experienceCost = getOrSetDefault("Settings.experience_cost", 10);
        experienceCostAll = getOrSetDefault("Settings.experience_cost_all", 15);

        boolean saveRequired = false;

        for (Messages message : Messages.values()) {
            if (!config.contains(message.path)) {
                config.set(message.path, message.defaultMessage);
                saveRequired = true;
            }
            message.loadMessage(config);
        }

        if (saveRequired) {
            config.save();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrSetDefault(String path, T defaultValue) {
        if (!config.contains(path)) {
            config.set(path, defaultValue);
            return defaultValue;
        }
        return (T) config.get(path);
    }

    public boolean isDisableAnvilInteraction() {
        return anvilInteraction;
    }

    public int getExperienceCost() {
        return experienceCost;
    }

    public int getExperienceCostAll() {
        return experienceCostAll;
    }
}

