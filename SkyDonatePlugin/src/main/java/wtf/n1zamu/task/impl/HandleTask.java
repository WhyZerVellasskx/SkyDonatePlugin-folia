package wtf.n1zamu.task.impl;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import wtf.n1zamu.api.PurchaseEvent;
import wtf.n1zamu.task.ITask;

@Setter
public class HandleTask implements ITask {
    @Override
    public Runnable getTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                getPurchases().forEach(purchase -> {
                    purchase.getUsedCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                    if (isDebugged()) {
                        Bukkit.getLogger().info("[SkyDonate] Выдана покупка для игрока: " + purchase.getUsername());
                    }
                    Bukkit.getPluginManager().callEvent(new PurchaseEvent(purchase));
                });
                getPurchases().clear();
            }
        };
    }
}
