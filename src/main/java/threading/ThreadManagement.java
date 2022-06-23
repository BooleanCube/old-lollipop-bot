package threading;

import awatch.controller.ALoader;
import lollipop.BotStatistics;
import mread.controller.RLoader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manages all the threads being used
 * (For now only 1 thread shall be used)
 */
public class ThreadManagement {

    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    static {
        executor.setMaximumPoolSize(10);
    }

    /**
     * Execute a call from the API or from ALoader
     * @param runnable runnable action
     */
    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * Setup a cache refresh cycle which refreshes the cache every day so the new animes and new information reaches as soon as possible
     */
    public static void setupCacheRefresh() {
        Runnable apiRefresh = () -> {
            ALoader.animeCache.clear();
            ALoader.characterCache.clear();
            RLoader.mangaCache.clear();
        };
        scheduler.scheduleWithFixedDelay(apiRefresh, 1, 1, TimeUnit.DAYS);
    }

    /**
     * Setup a statistics refresh cycle to reset the statistics on the bot list websites
     */
    public static void setupStatisticsCycle() {
        Runnable statsRefresh = BotStatistics::setStatistics;
        scheduler.scheduleWithFixedDelay(statsRefresh, 6, 12, TimeUnit.HOURS);
    }

}
