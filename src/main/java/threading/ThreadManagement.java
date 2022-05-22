package threading;

import awatch.controller.ALoader;
import lollipop.API;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manages all the threads being used
 * (For now only 1 thread shall be used)
 */
public class ThreadManagement {

    public static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public static void setupCacheRefresh() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable apiRefresh = () -> {
            ALoader.animeCache.clear();
            API.mangaCache.clear();
        };
        // Start API refresh cycle once a day to refresh animes everyday to make sure new data is cached later on
        scheduler.scheduleWithFixedDelay(apiRefresh, 1, 1, TimeUnit.DAYS);
    }

}
