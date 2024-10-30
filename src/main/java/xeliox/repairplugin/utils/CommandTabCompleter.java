package xeliox.repairplugin.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandTabCompleter implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("repairplugin")) {
            if (args.length == 1) {
                completions.add("reload");
            }
        } else if (command.getName().equalsIgnoreCase("giveexp")) {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            } else if (args.length == 2) {
                completions.add("1");
                completions.add("2");
                completions.add("3");
                completions.add("4");
                completions.add("5");
                completions.add("6");
                completions.add("7");
                completions.add("8");
                completions.add("9");
                completions.add("10");
                completions.add("20");
                completions.add("30");
                completions.add("40");
                completions.add("50");
                completions.add("60");
                completions.add("70");
                completions.add("80");
                completions.add("90");
                completions.add("100");
            }
        } else if (command.getName().equalsIgnoreCase("repair")) {
            if (args.length == 1) {
                completions.add("all");
            }
        }
        return completions;
    }
}
