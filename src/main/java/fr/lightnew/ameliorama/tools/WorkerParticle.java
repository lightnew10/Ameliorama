package fr.lightnew.ameliorama.tools;

import fr.lightnew.ameliorama.Ameliorama;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WorkerParticle extends BukkitRunnable {
    private Queue<Runnable> todo = new ArrayDeque<>();

    @Override
    public void run() {
        if (todo.isEmpty())
            return;
        Runnable current = todo.poll();
        current.run();
    }

    public void put(Location start, Location locSoul) {
        if (!start.getWorld().getName().equalsIgnoreCase(locSoul.getWorld().getName()))
            return;
        CompletableFuture.runAsync(() -> {
            CompletableFuture<Void> f = new CompletableFuture<>();
            f.completeOnTimeout(null, 200, TimeUnit.MILLISECONDS).thenRunAsync(() -> {

                double distance = start.distance(locSoul);
                double steps = distance * 2.5;
                start.add(0, 1, 0);
                locSoul.add(0, .5, 0);

                double deltaX = (locSoul.getX() - start.getX()) / steps;
                double deltaY = (locSoul.getY() - start.getY()) / steps;
                double deltaZ = (locSoul.getZ() - start.getZ()) / steps;

                new BukkitRunnable() {
                    int currentStep = 0;

                    final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.PURPLE, 2.0F);
                    @Override
                    public void run() {
                        if (currentStep >= steps) {
                            this.cancel();
                            return;
                        }
                        start.add(deltaX, deltaY, deltaZ);
                        start.getWorld().spawnParticle(Particle.REDSTONE, start, 1, dustOptions);
                        currentStep++;
                    }
                }.runTaskTimer(Ameliorama.instance, 0L, 1L);
            });
        }, Executors.newCachedThreadPool()).thenRun(() -> {
        });
    }
}
