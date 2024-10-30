package xeliox.repairplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import xeliox.repairplugin.core.ConfigManager;
import xeliox.repairplugin.core.Messages;
import xeliox.repairplugin.listener.RepairPluginListener;
import xeliox.repairplugin.utils.ColorTranslator;
import xeliox.repairplugin.utils.CommandTabCompleter;
import xeliox.repairplugin.utils.VersionUtils;

public class RepairPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private final PluginDescriptionFile pdfFile = getDescription();
    private final String version = pdfFile.getVersion();
    public static VersionUtils versionUtils;
    private final String author = getDescription().getAuthors().isEmpty() ? "Unknown" : getDescription().getAuthors().get(0);

    @Override
    public void onEnable() {
        setVersion();
        saveDefaultConfig();
        configManager = new ConfigManager(this);

        console.sendMessage(ColorTranslator.translate(Messages.PREFIX.getMessage() + " &aHas been enabled! &fVersion: " + version));
        console.sendMessage(ColorTranslator.translate(Messages.PREFIX.getMessage()+ " &fPlugin Creator &c" + author));

        MainCommand mainCommand = new MainCommand(this);
        CommandTabCompleter commandTabCompleter = new CommandTabCompleter();

        PluginCommand repairPlugin = getCommand("repairplugin");
        if (repairPlugin != null) {
            repairPlugin.setExecutor(mainCommand);
            repairPlugin.setTabCompleter(commandTabCompleter);
        }
        PluginCommand repair = getCommand("repair");
        if (repair != null) {
            repair.setExecutor(mainCommand);
            repair.setTabCompleter(commandTabCompleter);
        }
        PluginCommand giveexp = getCommand("giveexp");
        if (giveexp != null) {
            giveexp.setExecutor(mainCommand);
            giveexp.setTabCompleter(commandTabCompleter);
        }

        getServer().getPluginManager().registerEvents(new RepairPluginListener(configManager), this);
    }

    @Override
    public void onDisable() {
        console.sendMessage(ColorTranslator.translate(Messages.PREFIX.getMessage() + " &cHas been disabled! &fVersion: " + version));
        console.sendMessage(ColorTranslator.translate(Messages.PREFIX.getMessage() + " &fPlugin Creator &c" + author));
    }

    public void setVersion(){
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String bukkitVersion = Bukkit.getServer().getBukkitVersion().split("-")[0];
        switch(bukkitVersion){
            case "1.20.5":
            case "1.20.6":
                versionUtils = VersionUtils.v1_20_R4;
                break;
            case "1.21":
            case "1.21.1":
                versionUtils = VersionUtils.v1_21_R1;
                break;
            default:
                versionUtils = VersionUtils.valueOf(packageName.replace("org.bukkit.craftbukkit.", ""));
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
