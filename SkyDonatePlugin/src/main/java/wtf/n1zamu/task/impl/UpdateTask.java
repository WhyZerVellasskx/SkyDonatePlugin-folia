package wtf.n1zamu.task.impl;

import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.n1zamu.SkyDonatePlugin;
import wtf.n1zamu.http.Request;
import wtf.n1zamu.task.ITask;

@Setter
public class UpdateTask implements ITask {
    private String SERVER, STORE, SECRET_KEY;

    public UpdateTask(SkyDonatePlugin skyDonatePlugin) {
        SERVER = skyDonatePlugin.getConfig().getString("SERVER_ID");
        STORE = skyDonatePlugin.getConfig().getString("STORE_ID");
        SECRET_KEY = skyDonatePlugin.getConfig().getString("SECRET_KEY");
    }

    @Override
    public Runnable getTask() {
        return new BukkitRunnable() {
            final Request request = new Request("https://api.skydonate.ru/api/v2/method/surcharge.update");
            @Override
            public void run() {
                if (getUpdateCache().asMap().isEmpty()) {
                    return;
                }
                request.sendData(getUpdateCache().asMap(), SERVER, STORE, SECRET_KEY);
                getUpdateCache().asMap().clear();
            }
        };
    }
}
