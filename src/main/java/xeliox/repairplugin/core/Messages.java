package xeliox.repairplugin.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import xeliox.repairplugin.utils.ColorTranslator;

public enum Messages {
    PREFIX("Messages.prefix", "&8[&dRepairPlugin&8] &r"),
    USE_RELOAD("Messages.use_reload", "&eUse: /repairplugin reload"),
    USE_REPAIR("Messages.use_repair", "&eUse: /repair [all]"),
    USE_GIVEEXP("Messages.use_giveexp", "&cUse: /giveexp <player> <amount>"),
    NOT_ENOUGH_EXP("Messages.not_enough_exp", "&cYou don't have enough experience levels!"),
    NOT_ENOUGH_EXP_ALL("Messages.not_enough_exp_all", "&cYou don't have enough experience levels to repair all the items!"),
    RELOAD_CONFIG("Messages.reload_config", "&aConfiguration reloaded successfully!"),
    NO_PERMISSION("Messages.no_permission", "&cYou do not have sufficient permissions to do that."),
    NO_PERMISSION_REPAIR("Messages.no_repair_permission", "&cYou need rank &8[&eVIP&8] &co higher to repair items."),
    NO_PERMISSION_GIVEEXP("Messages.no_giveexp_permission", "&cYou do not have permissions to give experience to another player."),
    ITEM_REPAIRED("Messages.item_repaired", "&aItem repaired successfully. &e{amount} &alevels of experience consumed."),
    ITEMS_REPAIRED("Messages.items_repaired", "&aAll items have been repaired. &e{amount} &alevels of experience have been consumed."),
    NO_ITEM_HAND("Messages.no_item_hand", "&cYou do not have a damaged item in your hand."),
    NO_ITEMS("Messages.no_items", "&cYou have no damaged items in your inventory."),
    ERROR_CONSOLE("Messages.error_console", "&cThis command can only be executed by players."),
    NO_VALID_AMOUNT_EXP("Messages.no_valid_exp", "&cInvalid quantity. Must be a number."),
    GIVE_MESSAGE("Messages.experience_given", "&aYou have transferred &a{amount} &a levels to &e{target}&a."),
    RECEIVE_MESSAGE("Messages.experience_received", "&aYou have received &a{amount} &a levels of &a{source}&a."),
    PLAYER_NOT_FOUND("Messages.player_not_found", "&cPlayer not found."),
    ANVIL_REPAIR_MESSAGE("Messages.anvil_repair", "&aYour item has been automatically repaired."),
    PLAYER_EQUALS_TARGET("Messages.player_equals_target", "&cYou can't give yourself xp.");


    private final String path;
    private final String defaultMessage;
    private String message;

    Messages(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    public void loadMessage(@NotNull FileConfiguration config) {
        this.message = ColorTranslator.translate(config.getString(this.path, this.defaultMessage));
    }

    public String getMessage() {
        return message;
    }
}
