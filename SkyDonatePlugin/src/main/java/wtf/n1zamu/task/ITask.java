package wtf.n1zamu.task;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import wtf.n1zamu.SkyDonatePlugin;
import wtf.n1zamu.api.dto.Purchase;

import java.util.ArrayList;
import java.util.List;

public interface ITask {
    List<Purchase> purchases = new ArrayList<>();

    Cache<String, Integer> updateCache = CacheBuilder.newBuilder()
            .build();

    Runnable getTask();

    default List<Purchase> getPurchases() {
        return purchases;
    }

    default Cache<String, Integer> getUpdateCache() {
        return updateCache;
    }

    default void startTaskAsynchronously() {
        TaskExecutor.runTaskTimerAsync(this.getTask(), 0, 210);
    }

    default void startTask() {
        TaskExecutor.runTaskTimer(this.getTask(), 0, 10);
    }

    default boolean isDebugged() {
        return SkyDonatePlugin.getPlugin(SkyDonatePlugin.class).getConfig().getBoolean("debug");
    }
}
