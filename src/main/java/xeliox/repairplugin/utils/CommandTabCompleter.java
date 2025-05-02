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
                List<Integer> levels = new ArrayList<>();
                for (int i = 1; i <= 10; i++) levels.add(i);
                for (int i = 20; i <= 100; i += 10) levels.add(i);

                levels.sort(Integer::compareTo);
                for (Integer level : levels) {
                    completions.add(String.valueOf(level));
                }
            }
        } else if (command.getName().equalsIgnoreCase("repair")) {
            if (args.length == 1) {
                completions.add("all");
            }
        }
        return completions;
    }
}
