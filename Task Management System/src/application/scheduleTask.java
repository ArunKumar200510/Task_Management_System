package application;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class scheduleTask {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Calculate initial delay until the next 15-minute interval
        long initialDelay = calculateInitialDelay();

        // Run the task every 15 minutes
        scheduler.scheduleAtFixedRate(() -> {
            utility.executeTaskMoveProcess();
        }, initialDelay, 1, TimeUnit.MINUTES);
    }

    private static long calculateInitialDelay() {
        // Get the current time in minutes
        long currentTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());

        // Calculate the time until the next 15-minute interval
        long timeUntilNext15Minutes = 1 - (currentTimeMinutes % 1);

        // Convert to milliseconds for scheduling
        return TimeUnit.MINUTES.toMillis(timeUntilNext15Minutes);
    }
}
