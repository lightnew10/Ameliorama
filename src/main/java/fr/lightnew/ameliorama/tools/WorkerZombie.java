package fr.lightnew.ameliorama.tools;

import fr.lightnew.ameliorama.Ameliorama;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WorkerZombie extends BukkitRunnable {
    private Queue<Runnable> todo = new ArrayDeque<>();

    @Override
    public void run() {
        if (todo.isEmpty())
            return;
        Runnable current = todo.poll();
        current.run();
    }

    public void put() {
        CompletableFuture.runAsync(() -> {
            CompletableFuture<Void> f = new CompletableFuture<>();
            f.completeOnTimeout(null, 200, TimeUnit.MILLISECONDS).thenRunAsync(() -> {
            });
        }, Executors.newCachedThreadPool()).thenRun(() -> {
        });
    }
}
