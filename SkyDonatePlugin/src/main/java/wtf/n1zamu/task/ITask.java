package wtf.n1zamu.task;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.bukkit.Bukkit;

import org.bukkit.plugin.Plugin;
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

    default void startTaskAsynchronously(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.getTask(), 0, 210);
    }

    default void startTask(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this.getTask(), 0, 10);
    }

    default boolean isDebugged() {
        return SkyDonatePlugin.getPlugin(SkyDonatePlugin.class).getConfig().getBoolean("debug");
    }
}
