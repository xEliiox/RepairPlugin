package xeliox.repairplugin.core;

import org.bukkit.configuration.file.FileConfiguration;
import xeliox.repairplugin.RepairPlugin;

public class ConfigManager {
    private final RepairPlugin plugin;
    private boolean anvilInteraction;
    private int experienceCost;
    private int experienceCostAll;

    public ConfigManager(RepairPlugin plugin) {
        this.plugin = plugin;
        loadConfiguration();
    }

    public void loadConfiguration() {
        FileConfiguration config = plugin.getConfig();

        anvilInteraction = config.getBoolean("Settings.disableAnvilInteraction", false);
        experienceCost = config.getInt("Settings.experience_cost", 10);
        experienceCostAll = config.getInt("Settings.experience_cost_all", 15);

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

