package fr.lightnew.ameliorama.commands.admin;

import fr.lightnew.ameliorama.Ameliorama;
import fr.lightnew.ameliorama.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;

public class ConfigGame implements CommandExecutor, TabCompleter {

    private List<String> subCommand = Arrays.asList("start", "end", "reset", "spawners");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && sender.hasPermission("ameliorama.configgame")) {
            String error = ChatColor.RED + "Use this command like this /cg <" + String.join("/", subCommand) + ">";
            if (args.length == 0 || subCommand.contains(args[0].toLowerCase())) {
                player.sendMessage(error);
                return true;
            }
            if (args.length == 1) {
                //TEST TEMP
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline())
                            Ameliorama.spawners.forEach(location ->
                                    player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(Color.PURPLE, 2.0F)));
                        else
                            cancel();
                    }
                }.runTaskTimerAsynchronously(Ameliorama.instance, 0, 10);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return subCommand;
    }
}
