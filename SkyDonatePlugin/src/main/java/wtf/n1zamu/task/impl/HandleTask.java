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
                    for (int i = 0; i < purchase.getUsedCommands().size(); i++) {
                        if (isDebugged()) {
                            Bukkit.getLogger().info("[SkyDonate] Использую команду номер " + i + ": " + purchase.getUsedCommands().get(i));
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), purchase.getUsedCommands().get(i));
                    }
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
