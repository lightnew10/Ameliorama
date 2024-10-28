package fr.lightnew.ameliorama.commands;

import fr.lightnew.ameliorama.Ameliorama;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class GiveSpecialItem implements CommandExecutor, TabCompleter {

    private final Map<String, ItemStack> custom_items = Map.of(
            "wellofsoul", wellOfSoul()
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && player.hasPermission("ameliorama.gsi")) {
            String error = ChatColor.RED + "ErrorCommand !\n" + ChatColor.GOLD + "How to use this command :" + ChatColor.YELLOW + "\n/gsi " + custom_items.keySet();
            if (args.length == 0) {
                player.sendMessage(error);
                return false;
            }
            if (custom_items.keySet().stream().anyMatch(string -> args[0].toLowerCase().equalsIgnoreCase(string))) {
                player.getInventory().addItem(custom_items.get(args[0].toLowerCase()));
                player.sendMessage(ChatColor.YELLOW +"Item" + ChatColor.GRAY + " (" + args[0] + ") " + ChatColor.YELLOW + "has been given!");
            } else player.sendMessage(error);
        }
        return false;
    }

    private ItemStack wellOfSoul() {
        ItemStack itemStack = new ItemStack(Material.LIGHTNING_ROD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "WellOfSoul");
        meta.getPersistentDataContainer().set(new NamespacedKey(Ameliorama.instance, "wellofsoul"), PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return custom_items.keySet().stream().toList();
    }
}
