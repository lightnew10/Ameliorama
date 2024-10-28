package fr.lightnew.ameliorama.game;

public enum GameState {
    WAIT,
    GAME,
    END;

    private static GameState currentState;

    public static void setGameState(GameState state) {
        currentState = state;
    }

    public static GameState getCurrentGameState() {
        return currentState;
    }

    public static GameState stringToGameState(String value) {
        return switch (value.toLowerCase()) {
            case "wait" -> WAIT;
            case "game" -> GAME;
            case "end" -> END;
            default -> null;
        };
    }
}
