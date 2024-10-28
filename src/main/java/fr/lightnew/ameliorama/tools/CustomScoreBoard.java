package fr.lightnew.ameliorama.tools;

import fr.lightnew.ameliorama.Ameliorama;
import fr.lightnew.ameliorama.entities.PlayerManager;
import fr.lightnew.ameliorama.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.WeakHashMap;

public class CustomScoreBoard {

    public static WeakHashMap<Player, CustomScoreboardExtend> receivers = new WeakHashMap<>();

    public static void sendScoreboard() {
        switch (GameState.getCurrentGameState()) {
            case GAME -> scoreBoardGame("GAME");
            case END -> scoreBoardEnd("END");
            default -> scoreBoardWait("WAIT");
        }
    }

    private static void updateScoreboard(Player player, String title, String... lines) {
        CustomScoreboardExtend extend;
        if (!receivers.containsKey(player)) {
            extend = new CustomScoreboardExtend(title, player);
            receivers.put(player, extend);
        } else {
            extend = receivers.get(player);
            if (!extend.getTitle().equals(title)) {
                extend.removeScoreBoard();
                receivers.remove(player);
                extend = new CustomScoreboardExtend(title, player);
                receivers.put(player, extend);
            }
        }
        extend.setLines(lines);
    }

    private static void scoreBoardWait(String title) {
        Bukkit.getOnlinePlayers().forEach(player -> updateScoreboard(player, title,
                "Name : " + player.getName(),
                "current -  : " + new Date(),
                "Waiting players..."
        ));
    }

    private static void scoreBoardGame(String title) {
        Ameliorama.gameSettings.getPlayers().forEach(player -> {
            if (Ameliorama.gameSettings.getPlayerManagerList().containsKey(player.getUniqueId())) {
                PlayerManager playerManager = Ameliorama.gameSettings.getPlayerManagerList().get(player.getUniqueId());
                updateScoreboard(player, title,
                        "Name : " + player.getName(),
                        "Gold : " + playerManager.getGold(),
                        "emerald : " + playerManager.getEmerald(),
                        "Waiting players..."
                );
            } else
                scoreBoardWait("WAIT");
        });
    }

    private static void scoreBoardEnd(String title) {
        Bukkit.getOnlinePlayers().forEach(player -> updateScoreboard(player, title,
                "Name : " + player.getName(),
                "end game total ore : " + Ameliorama.gameSettings.getIron()
        ));
    }

}
