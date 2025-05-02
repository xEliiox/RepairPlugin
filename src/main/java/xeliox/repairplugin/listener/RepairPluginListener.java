package xeliox.repairplugin.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xeliox.repairplugin.core.ConfigManager;
import xeliox.repairplugin.core.Messages;
import xeliox.repairplugin.utils.VersionCheck;

public class RepairPluginListener implements Listener {

    private final ConfigManager configManager;

    public RepairPluginListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (configManager.isDisableAnvilInteraction()) {
            return;
        }

        Player player = event.getPlayer();
        int expCost = configManager.getAnvilExperienceCost();

        ItemStack itemInHand = event.getItem();
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ANVIL) {
            event.setCancelled(configManager.isEventAnvilInteraction());
            if (itemInHand != null && itemInHand.getType() != Material.AIR && isDamaged(itemInHand)) {
                if (player.getLevel() >= expCost) {
                    repairItem(itemInHand);
                    player.setLevel(player.getLevel() - expCost);
                    event.getPlayer().sendMessage(Messages.PREFIX.getMessage() + Messages.ANVIL_REPAIR_MESSAGE.getMessage());
                } else {
                    player.sendMessage(Messages.PREFIX.getMessage() + Messages.NOT_ENOUGH_EXP.getMessage());
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean isDamaged(@NotNull ItemStack item) {
        if (VersionCheck.serverIsLegacy()) {
            return item.getDurability() > 0;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof org.bukkit.inventory.meta.Damageable) {
            return ((org.bukkit.inventory.meta.Damageable) meta).hasDamage();
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    private void repairItem(@NotNull ItemStack item) {
        if (VersionCheck.serverIsLegacy()) {
            item.setDurability((short) 0);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof org.bukkit.inventory.meta.Damageable) {
            org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) meta;
            damageable.setDamage(0);
            item.setItemMeta(meta);
        }
    }
}
