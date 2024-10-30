package xeliox.repairplugin.listener;

import org.bukkit.Material;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xeliox.repairplugin.core.ConfigManager;
import xeliox.repairplugin.core.Messages;

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

        ItemStack itemInHand = event.getItem();
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ANVIL) {
            if (itemInHand != null && itemInHand.getType() != Material.AIR && isDamaged(itemInHand)) {
                repairItem(itemInHand);
                event.getPlayer().sendMessage(Messages.PREFIX.getMessage() + Messages.ANVIL_REPAIR_MESSAGE);
            }
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
            Damageable damageable = (Damageable) meta;
            damageable.setDamage(0);
            item.setItemMeta(meta);
        }
    }
}
