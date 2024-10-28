package fr.lightnew.ameliorama.entities;

import fr.lightnew.ameliorama.Ameliorama;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlayerManager {

    private double gold;
    private double emerald;

    public PlayerManager() {
        gold = 0;
        emerald = 0;
    }

    public void giveResourcesKillZombie() {
        switch (Ameliorama.gameSettings.getLvlDifficulty()) {
            case 0, 1: {
                gold = gold + 5;
                break;
            }
            case 2, 3: {
                gold = gold + 10;
                break;
            }
            case 4: {
                gold = gold + 20;
            }
            default: break;
        }
    }
}
