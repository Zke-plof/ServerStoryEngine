package com.serverstoryengine.commands;

import com.serverstoryengine.ServerStoryEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LandmarkCommand implements CommandExecutor {

    private final ServerStoryEngine plugin;

    public LandmarkCommand(ServerStoryEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("storyengine.landmark")) {
            player.sendMessage(Component.text("You don't have permission.", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            plugin.getLandmarkManager().showNearestLandmark(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "find" -> plugin.getLandmarkManager().showNearestLandmark(player);
            default -> {
                player.sendMessage(Component.text("Usage: /landmark [find]", NamedTextColor.RED));
            }
        }

        return true;
    }
}
