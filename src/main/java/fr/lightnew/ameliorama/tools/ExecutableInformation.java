package fr.lightnew.ameliorama.tools;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutableInformation {

    public static ScheduledExecutorService runCustomTask(Runnable task, TimeUnit timeToRepeat, long delay, long period) {
        ScheduledExecutorService run = Executors.newScheduledThreadPool(3);
        run.scheduleAtFixedRate(() -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay, period, timeToRepeat);
        return run;
    }

}
