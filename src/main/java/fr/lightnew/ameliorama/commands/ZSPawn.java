package fr.lightnew.ameliorama.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class ZSPawn implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && player.hasPermission("ameliorama.zspawn")) {
            if (args.length == 0) {
                Entity entity = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
                //SpigotPathFinder ai = new SpigotPathFinder(Ameliorama.instance, (LivingEntity) entity);
                //ai.isActive();
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
