package xeliox.repairplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xeliox.repairplugin.core.ConfigManager;
import xeliox.repairplugin.core.Messages;

import java.io.IOException;

public class MainCommand implements CommandExecutor {

    private final RepairPlugin plugin;
    private final ConfigManager configManager;

    public MainCommand(@NotNull RepairPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("repairplugin")) {
            try {
                return handleRepairPluginCommand(sender, args);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (command.getName().equalsIgnoreCase("repair")) {
            return handleRepairCommand(sender, args);
        } else if (command.getName().equalsIgnoreCase("giveexp")) {
            return handleGiveExpCommand(sender, args);
        }
        return false;
    }

    private boolean handleRepairPluginCommand(CommandSender sender, String @NotNull [] args) throws IOException {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("repairplugin.admin")) {
                sender.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_PERMISSION.getMessage());
                return false;
            }

            plugin.reloadConfig();
            configManager.loadConfiguration();
            sender.sendMessage(Messages.PREFIX.getMessage() + Messages.RELOAD_CONFIG.getMessage());
        } else {
            sender.sendMessage(Messages.PREFIX.getMessage() + Messages.USE_RELOAD.getMessage());
        }
        return true;
    }

    private boolean handleRepairCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX.getMessage() + Messages.ERROR_CONSOLE.getMessage());
            return false;
        }

        Player player = (Player) sender;
        if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
            if (!player.hasPermission("repairplugin.repair.all")) {
                player.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_PERMISSION_REPAIR_ALL.getMessage());
                return false;
            }
            repairAllItems(player);
            return true;
        }


        if (!player.hasPermission("repairplugin.repair")) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_PERMISSION_REPAIR.getMessage());
            return false;
        }

        if (args.length == 0) {
            repairSingleItem(player);
            return true;
        } else {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.USE_REPAIR.getMessage());
            return false;
        }
    }

    private boolean handleGiveExpCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX.getMessage() + Messages.ERROR_CONSOLE.getMessage());
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("repairplugin.giveexp")) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_PERMISSION_GIVEEXP.getMessage());
            return false;
        }

        if (args.length != 2) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.USE_GIVEEXP.getMessage());
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.PLAYER_NOT_FOUND.getMessage());
            return false;
        }

        if (target == player) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.PLAYER_EQUALS_TARGET.getMessage());
            return false;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_VALID_AMOUNT_EXP.getMessage());
            return false;
        }

        if (player.getLevel() < amount) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.NOT_ENOUGH_EXP.getMessage());
            return false;
        }

        if (amount <= 0) {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_VALID_AMOUNT_EXP.getMessage());
            return false;
        }

        player.setLevel(player.getLevel() - amount);
        target.setLevel(target.getLevel() + amount);

        player.sendMessage(Messages.PREFIX.getMessage() + Messages.GIVE_MESSAGE.getMessage()
                .replace("{amount}", String.valueOf(amount))
                .replace("{target}", target.getName()));
        target.sendMessage(Messages.PREFIX.getMessage() + Messages.RECEIVE_MESSAGE.getMessage()
                .replace("{amount}", String.valueOf(amount))
                .replace("{source}", player.getName()));
        return true;
    }

    private void repairSingleItem(@NotNull Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.AIR && isDamaged(itemInHand)) {
            if (player.getLevel() >= configManager.getExperienceCost()) {
                repairItem(itemInHand);
                player.setLevel(player.getLevel() - configManager.getExperienceCost());
                player.sendMessage(Messages.PREFIX.getMessage() + Messages.ITEM_REPAIRED.getMessage()
                        .replace("{amount}", String.valueOf(configManager.getExperienceCost())));
            } else {
                player.sendMessage(Messages.PREFIX.getMessage() + Messages.NOT_ENOUGH_EXP.getMessage());
            }
        } else {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_ITEM_HAND.getMessage());
        }
    }

    private void repairAllItems(@NotNull Player player) {
        ItemStack[] inventoryContents = player.getInventory().getContents();
        boolean hasDamagedItems = false;

        for (ItemStack item : inventoryContents) {
            if (item != null && item.getType() != Material.AIR && isDamaged(item)) {
                hasDamagedItems = true;
                break;
            }
        }

        if (hasDamagedItems) {
            if (player.getLevel() >= configManager.getExperienceCostAll()) {
                for (ItemStack item : inventoryContents) {
                    if (item != null && item.getType() != Material.AIR && isDamaged(item)) {
                        repairItem(item);
                    }
                }
                player.setLevel(player.getLevel() - configManager.getExperienceCostAll());
                player.sendMessage(Messages.PREFIX.getMessage() + Messages.ITEMS_REPAIRED.getMessage()
                        .replace("{amount}", String.valueOf(configManager.getExperienceCostAll())));
            } else {
                player.sendMessage(Messages.PREFIX.getMessage() + Messages.NOT_ENOUGH_EXP_ALL.getMessage());
            }
        } else {
            player.sendMessage(Messages.PREFIX.getMessage() + Messages.NO_ITEMS.getMessage());
        }
    }

    private boolean isDamaged(@NotNull ItemStack item) {
        if (item.getItemMeta() instanceof Damageable) {
            Damageable damageable = (Damageable) item.getItemMeta();
            return damageable != null && damageable.hasDamage();
        }
        return false;
    }

    private void repairItem(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable) {
            ((Damageable) meta).setDamage(0);
            item.setItemMeta(meta);
        }
    }
}
