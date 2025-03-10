package wtf.n1zamu.task;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class TaskExecutor {

    @Setter
    private static Plugin plugin;

    @Setter
    private static FoliaLib foliaLib;

    private TaskExecutor() {

    }

    public static void runTask(Runnable runnable) {
        if (foliaLib.isFolia()) {
            foliaLib.getScheduler().runNextTick(task -> runnable.run());
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTask(plugin);
        }
    }

    public static void runTask(Runnable runnable, Location location) {
        if (foliaLib.isFolia()) {
            foliaLib.getScheduler().runAtLocation(location, task -> runnable.run());
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTask(plugin);
        }
    }

    public static void runTaskAsync(Runnable runnable) {
        if (foliaLib.isFolia()) {
            foliaLib.getScheduler().runAsync(task -> runnable.run());
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        if (foliaLib.isFolia()) {
            foliaLib.getScheduler().runLater(runnable, delay);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTaskLater(plugin, delay);
        }
    }

    public static void runTaskLaterAsync(Runnable runnable, long delay) {
        if (foliaLib.isFolia()) {
            foliaLib.getScheduler().runLaterAsync(runnable, delay);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTaskLaterAsynchronously(plugin, delay);
        }
    }

    public static CancellableTask runTaskTimer(Runnable runnable, long delay, long period) {
        if (foliaLib.isFolia()) {
            WrappedTask wrappedTask = foliaLib.getScheduler().runTimer(runnable, delay, period);
            return createTask(wrappedTask);
        } else {
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            };
            bukkitRunnable.runTaskTimer(plugin, delay, period);
            return createTask(bukkitRunnable);
        }
    }

    public static CancellableTask runTaskTimerAsync(Runnable runnable, long delay, long period) {
        if (foliaLib.isFolia()) {
            WrappedTask wrappedTask = foliaLib.getScheduler().runTimerAsync(runnable, delay, period);
            return createTask(wrappedTask);
        } else {
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            };
            bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
            return createTask(bukkitRunnable);
        }
    }

    private static CancellableTask createTask(WrappedTask wrappedTask) {
        return new CancellableTask() {
            @Override
            public void cancel() {
                wrappedTask.cancel();
            }

            @Override
            public boolean isCancelled() {
                return wrappedTask.isCancelled();
            }
        };
    }

    private static CancellableTask createTask(BukkitRunnable bukkitRunnable) {
        return new CancellableTask() {
            @Override
            public void cancel() {
                bukkitRunnable.cancel();
            }

            @Override
            public boolean isCancelled() {
                return bukkitRunnable.isCancelled();
            }
        };
    }
}
