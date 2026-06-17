package com.serverstoryengine.commands;

import com.serverstoryengine.ServerStoryEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChronicleCommand implements CommandExecutor {

    private final ServerStoryEngine plugin;

    public ChronicleCommand(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("storyengine.chronicle")) {
            player.sendMessage(Component.text("You don't have permission.", NamedTextColor.RED));
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            plugin.getChronicleGUI().open(player, 0);
            return true;
        }

        int page = 0;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("Invalid page number.", NamedTextColor.RED));
                return true;
            }
        }

        plugin.getChronicleManager().sendChronicleToPlayer(player, page);
        return true;
    }
}
