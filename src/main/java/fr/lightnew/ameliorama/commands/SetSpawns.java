package fr.lightnew.ameliorama.commands;

import fr.lightnew.ameliorama.Ameliorama;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class SetSpawns implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && player.hasPermission("ameliorama.setspawn")) {
            String error = ChatColor.RED + "ErrorCommand !\n" + ChatColor.GOLD + "How to use this command :" + ChatColor.YELLOW + "\n/setspawn <option> (safe_zone, game_zone, spawners)";
            if (args.length == 0) {
                player.sendMessage(error);
                return false;
            }
            Location location = player.getLocation();
            switch (args[0]) {
                case "safe_zone": {
                    setLocation("spawns.safe-zone", location);
                    Ameliorama.spawnSafeZone = location;
                    player.sendMessage(ChatColor.YELLOW + "Location of Safe zone set !");
                    return true;
                }
                case "game_zone": {
                    setLocation("spawns.game-zone", location);
                    Ameliorama.spawnGameZone = location;
                    player.sendMessage(ChatColor.YELLOW + "Location of Game zone set !");
                    return true;
                }
                case "spawners": {
                    Ameliorama.spawners.add(location);
                    Ameliorama.instance.getConfig().set("spawns.spawners", Ameliorama.spawners);
                    Ameliorama.instance.saveConfig();
                    player.sendMessage(ChatColor.YELLOW + "Location of spawner set ! (" + Ameliorama.spawners.size()  +")");
                    player.sendMessage(ChatColor.GRAY + "If you want to create other spawn of spawners you can re send this command");
                    return true;
                }
                default: {
                    player.sendMessage(error);
                    return false;
                }
            }
        }
        return false;
    }

    private void setLocation(String idConfig, Location location) {
        Ameliorama.instance.getConfig().set(idConfig + ".world", location.getWorld().getName());
        Ameliorama.instance.getConfig().set(idConfig + ".x", location.getX());
        Ameliorama.instance.getConfig().set(idConfig + ".y", location.getY());
        Ameliorama.instance.getConfig().set(idConfig + ".z", location.getZ());
        Ameliorama.instance.getConfig().set(idConfig + ".yaw", location.getYaw());
        Ameliorama.instance.getConfig().set(idConfig + ".pitch", location.getPitch());
        Ameliorama.instance.saveConfig();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return List.of("safe_zone", "game_zone", "spawners");
        return List.of();
    }
}
