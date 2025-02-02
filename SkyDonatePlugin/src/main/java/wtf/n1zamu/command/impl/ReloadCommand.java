package wtf.n1zamu.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import wtf.n1zamu.SkyDonatePlugin;
import wtf.n1zamu.command.CommandAnnotation;
import wtf.n1zamu.command.ExecutableCommand;

@CommandAnnotation(getName = "reload", getNeedArgs = 1, getUsage = "&cИспользуйте /skydonate reload")
public class ReloadCommand extends ExecutableCommand {
    private final SkyDonatePlugin plugin;

    public ReloadCommand(SkyDonatePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("skydonate.admin")) {
            return;
        }
        if (!args[0].equalsIgnoreCase(this.getName())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getUsage()));
            return;
        }
        if (args.length != this.getNeedArgs()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getUsage()));
            return;
        }
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Конфиг успешно перезагружен!");

    }
}
