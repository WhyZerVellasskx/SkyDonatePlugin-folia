package wtf.n1zamu;

import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import wtf.n1zamu.command.CommandManager;
import wtf.n1zamu.config.DefaultConfiguration;
import wtf.n1zamu.listener.NodeMutateListener;
import wtf.n1zamu.listener.PlayerJoinListener;
import wtf.n1zamu.task.TaskExecutor;
import wtf.n1zamu.task.impl.*;

import java.io.File;

@Getter
public final class SkyDonatePlugin extends JavaPlugin {
    private CheckTask checkTask;
    private HandleTask handleTask;
    private UpdateTask updateTask;
    private DefaultConfiguration defaultConfiguration;

    @Override
    public void onLoad() {
        File defaultConfig = new File(this.getConfig().getCurrentPath());
        if (defaultConfig.exists()) return;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        FoliaLib foliaLib = new FoliaLib(this);
        TaskExecutor.setPlugin(this);
        TaskExecutor.setFoliaLib(foliaLib);

        this.defaultConfiguration = new DefaultConfiguration(this);

        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            LuckPerms api = null;
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

            if (provider != null) {
                api = provider.getProvider();
            }

            if (api != null) {
                updateTask = new UpdateTask(this);
                new NodeMutateListener(this, api);
                Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this, api), this);
                updateTask.startTaskAsynchronously();
            }
        } else {
            Bukkit.getLogger().info("[SkyDonate] LuckPerms API не загружена! Доплата не будет работать");
        }

        new CommandManager(this);
        checkTask = new CheckTask(this);
        handleTask = new HandleTask();
        checkTask.startTaskAsynchronously();
        handleTask.startTask();

        Bukkit.getLogger().info("[SkyDonate] Плагин успешно загружен!");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getLogger().info("[SkyDonate] Плагин отгружен!");
    }
}
