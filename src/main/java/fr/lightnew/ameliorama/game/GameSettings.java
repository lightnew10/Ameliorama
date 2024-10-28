package fr.lightnew.ameliorama.game;

import fr.lightnew.ameliorama.Ameliorama;
import fr.lightnew.ameliorama.entities.PlayerManager;
import fr.lightnew.ameliorama.entities.WellOfSoul;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
@ToString
public class GameSettings {

    private GameDifficulty difficulty;
    private int minPlayerToStart;
    private int maxPlayer;
    private Long startAt;
    private Long endAt;
    private List<WellOfSoul> wellOfSoulList;
    private List<Player> players;
    private Map<UUID, PlayerManager> playerManagerList;

    //Resources
    private double wood = 0;
    private double stone = 0;
    private double iron = 0;

    //Upgradable
    private int lvlDifficulty = 0;
    private int lvlShield = 0;
    private int lvlRevive = 0;
    private int lvlGold = 0;
    private int lvlWood = 0;
    private int lvlStone = 0;
    private int lvlIron = 0;

    //Messages
    private final String noSpawnError = ChatColor.RED + "==========ERROR==========\n" + ChatColor.RED + "All spawn not set!\n" + ChatColor.YELLOW + "Please set spawn with /setspawn <option>\n" + ChatColor.RED + "==========ERROR==========";
    private final String credit = ChatColor.YELLOW + "By Lightnew, inspired by Map zombie BO3 'Ameliorama' by @mr_gogu (https://steamcommunity.com/id/mr_gogu)";

    public GameSettings(GameDifficulty difficulty) {
        this.difficulty = difficulty;
        this.minPlayerToStart = 1;
        this.maxPlayer = 4;
        wellOfSoulList = new ArrayList<>();
        players = new ArrayList<>();
        playerManagerList = new HashMap<>();
    }

    public void startGame() {
        if (Ameliorama.spawnGameZone == null || Ameliorama.spawnSafeZone == null || (Ameliorama.spawners == null || Ameliorama.spawners.isEmpty())) {
            Bukkit.broadcastMessage(noSpawnError);
            return;
        }
        players.addAll(Bukkit.getOnlinePlayers());
        players.forEach(player -> playerManagerList.put(player.getUniqueId(), new PlayerManager()));
        startAt = System.currentTimeMillis();
        GameState.setGameState(GameState.GAME);
    }

    public void endGame() {
        endAt = System.currentTimeMillis();
        GameState.setGameState(GameState.END);
        Bukkit.broadcastMessage(credit);
    }

    public Player getALiveRandomPlayer() {
        return players.stream().filter(player1 -> player1.isOnline() && player1.getGameMode().equals(GameMode.ADVENTURE)).findFirst().get();
    }

    public void announce(String title, String description) {
        players.forEach(player -> {
            if (player.isOnline())
                player.sendTitle(title, description, 5, 10, 5);
        });
    }
}
