package fr.lightnew.ameliorama;

import fr.lightnew.ameliorama.commands.ForceGame;
import fr.lightnew.ameliorama.commands.GiveSpecialItem;
import fr.lightnew.ameliorama.commands.SetSpawns;
import fr.lightnew.ameliorama.commands.ZSPawn;
import fr.lightnew.ameliorama.commands.admin.ConfigGame;
import fr.lightnew.ameliorama.game.GameDifficulty;
import fr.lightnew.ameliorama.game.GameSettings;
import fr.lightnew.ameliorama.game.GameState;
import fr.lightnew.ameliorama.listeners.PlayerEvents;
import fr.lightnew.ameliorama.listeners.WorldManagerEvents;
import fr.lightnew.ameliorama.tools.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Ameliorama extends JavaPlugin {

    public static Ameliorama instance;
    public static Location spawnSafeZone;
    public static Location spawnGameZone;
    public static List<Location> spawners;
    public static GameSettings gameSettings;
    public static WorkerParticle workerParticle;
    public static WorkerZombie workerZombie;
    private ScheduledExecutorService threadCustomScoreboard;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        gameSettings = new GameSettings(GameDifficulty.NORMAL);
        GameState.setGameState(GameState.WAIT);
    }

    @Override
    public void onEnable() {
        //Thread only for all particle (here for big request of particle)
        workerParticle = new WorkerParticle();
        workerParticle.runTaskTimerAsynchronously(this, 0, 10);
        //Thread for IA Zombie
        workerZombie = new WorkerZombie();
        workerZombie.runTaskTimerAsynchronously(this, 0, 10);
        //Commands
        getCommand("givespecialitem").setExecutor(new GiveSpecialItem());
        getCommand("givespecialitem").setTabCompleter(new GiveSpecialItem());
        getCommand("forcegame").setExecutor(new ForceGame());
        getCommand("setspawn").setExecutor(new SetSpawns());
        getCommand("setspawn").setTabCompleter(new SetSpawns());
        getCommand("zspawn").setExecutor(new ZSPawn());
        getCommand("configgame").setExecutor(new ConfigGame());
        getCommand("configgame").setTabCompleter(new ConfigGame());
        //Events
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerEvents(), this);
        manager.registerEvents(new WorldManagerEvents(), this);
        //Load spawns
        loadSpawns();
        //Custom scoreboard
        threadCustomScoreboard = ExecutableInformation.runCustomTask(CustomScoreBoard::sendScoreboard, TimeUnit.MILLISECONDS, 0, 500);
    }

    @Override
    public void onDisable() {
        CustomScoreBoard.receivers.forEach((player, customScoreboardExtend) -> customScoreboardExtend.removeScoreBoard());
        threadCustomScoreboard.close();
    }

    public void loadSpawns() {
        Location safe = getLoc("spawns.safe-zone");
        Location game = getLoc("spawns.game-zone");
        List<Location> spawnersList = (List<Location>) getConfig().getList("spawns.spawners");
        if (safe != null)
            spawnSafeZone = safe;
        if (game != null)
            spawnGameZone = game;
        if (spawnersList != null)
            spawners = spawnersList;
        else spawners = new ArrayList<>();
    }

    private Location getLoc(String loc) {
        String world = getConfig().getString(loc + ".world");
        double x = getConfig().getDouble(loc + ".x");
        double y = getConfig().getDouble(loc + ".y");
        double z = getConfig().getDouble(loc + ".z");
        double yaw = getConfig().getDouble(loc + ".yaw");
        double pitch = getConfig().getDouble(loc + ".pitch");
        if (world == null)
            return null;
        return new Location(Bukkit.getWorld(world), x,y,z, (float) yaw, (float) pitch);
    }
}
