package xeliox.repairplugin.core;

import org.simpleyaml.configuration.file.YamlFile;
import xeliox.repairplugin.RepairPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class ConfigManager {
    private final RepairPlugin plugin;
    private final YamlFile config;
    private boolean anvilInteraction;
    private int experienceCost;
    private int experienceCostAll;

    public ConfigManager(RepairPlugin plugin, Logger logger, YamlFile config) {
        this.plugin = plugin;
        this.config = config;
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                logger.warning("The configuration file does not exist!");
                logger.info("Creating a new configuration file...");

                InputStream defaultConfigStream = plugin.getResource("config.yml");
                if (defaultConfigStream != null) {
                    Files.copy(defaultConfigStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    logger.info("Default configuration file copied successfully.");
                } else {
                    logger.info("Default configuration file not found in the JAR!");
                }

            } catch (IOException e) {
                logger.severe("Failed to create config.yml file: " + e.getMessage());
            }
        }


        loadConfiguration();
    }

    public void loadConfiguration() {

        anvilInteraction = getOrSetDefault(config,"Settings.disableAnvilInteraction", false);
        experienceCost = getOrSetDefault(config,"Settings.experience_cost", 10);
        experienceCostAll = getOrSetDefault(config,"Settings.experience_cost_all", 15);

        boolean saveRequired = false;

        for (Messages message : Messages.values()) {
            if (!config.contains(message.path)) {
                config.set(message.path, message.defaultMessage);
                saveRequired = true;
            }
            message.loadMessage(config);
        }

        if (saveRequired) {
            plugin.saveConfig();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrSetDefault(YamlFile config, String path, T defaultValue) {
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

