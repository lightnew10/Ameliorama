package fr.lightnew.ameliorama.listeners;

import fr.lightnew.ameliorama.Ameliorama;
import fr.lightnew.ameliorama.game.GameState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        if (GameState.getCurrentGameState().equals(GameState.WAIT)) {
        }
        if (GameState.getCurrentGameState().equals(GameState.GAME) && !Ameliorama.gameSettings.getPlayers().contains(player)) {
            player.kickPlayer("Game is already started !");
            return;
        }
        if (Ameliorama.spawnSafeZone != null)
            player.teleport(Ameliorama.spawnSafeZone, PlayerTeleportEvent.TeleportCause.PLUGIN);
        else
            player.teleport(player.getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        event.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + player.getName());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isEndOrGameState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (isEndOrGameState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
    }

    private boolean isEndOrGameState() {
        return GameState.getCurrentGameState().equals(GameState.GAME) || GameState.getCurrentGameState().equals(GameState.END);
    }
}
