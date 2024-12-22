package xeliox.repairplugin.core;

import org.simpleyaml.configuration.file.YamlFile;
import xeliox.repairplugin.RepairPlugin;

public class ConfigManager {
    private final RepairPlugin plugin;
    private final YamlFile config;
    private boolean anvilInteraction;
    private int experienceCost;
    private int experienceCostAll;

    public ConfigManager(RepairPlugin plugin, YamlFile config) {
        this.plugin = plugin;
        this.config = config;
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

