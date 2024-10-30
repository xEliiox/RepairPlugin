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

        for (Messages message : Messages.values()) {
            message.loadMessage(config);
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

