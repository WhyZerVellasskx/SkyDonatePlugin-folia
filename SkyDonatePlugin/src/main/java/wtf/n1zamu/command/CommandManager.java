package wtf.n1zamu.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.n1zamu.SkyDonatePlugin;
import wtf.n1zamu.command.impl.ConfigureCommand;
import wtf.n1zamu.command.impl.ReloadCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final ConfigureCommand configureCommand;
    private final ReloadCommand reloadCommand;

    public CommandManager(SkyDonatePlugin plugin) {
        this.configureCommand = new ConfigureCommand(plugin);
        this.reloadCommand = new ReloadCommand(plugin);
        PluginCommand command = plugin.getCommand("skydonate");
        if (command == null) {
            return;
        }
        command.setTabCompleter(this);
        command.setExecutor(this);
        Bukkit.getLogger().info("[SkyDonate] Команды установлены!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        Arrays.asList(configureCommand, reloadCommand).forEach(executableCommand -> {
            if (args[0].equalsIgnoreCase(executableCommand.getName())) {
                executableCommand.execute(sender, args);
            }
        });
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "setup");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("setup")) {
            return Collections.singletonList("<shop id>");
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("setup")) {
            return Collections.singletonList("<server id>");
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("setup")) {
            return Collections.singletonList("<secret key>");
        }
        return Collections.emptyList();
    }
}
