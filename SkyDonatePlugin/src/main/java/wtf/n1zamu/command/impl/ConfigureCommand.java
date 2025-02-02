package wtf.n1zamu.command.impl;

import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import wtf.n1zamu.SkyDonatePlugin;
import wtf.n1zamu.command.CommandAnnotation;
import wtf.n1zamu.command.ExecutableCommand;
import wtf.n1zamu.http.Request;

@CommandAnnotation(getName = "setup", getNeedArgs = 4, getUsage = "&cИспользуйте /skydonate setup <id shop> <id server> <key>")
public class ConfigureCommand extends ExecutableCommand {
    private final SkyDonatePlugin plugin;

    public ConfigureCommand(SkyDonatePlugin plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
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
        String store_id = args[1];
        String server = args[2];
        String secretKey = args[3];

        plugin.getCheckTask().setRequest(new Request(String.format(
                "https://api.skydonate.ru/api/v2/method/orders.received?server_id=%s&store_id=%s&secret_key=%s",
                server,
                store_id,
                secretKey)));
        plugin.getDefaultConfiguration().saveConfigWithCommentaries(store_id, server, secretKey);
        plugin.reloadConfig();
        if (plugin.getUpdateTask() != null) {
            plugin.getUpdateTask().setSECRET_KEY(secretKey);
            plugin.getUpdateTask().setSTORE(store_id);
            plugin.getUpdateTask().setSERVER(server);
        }
        sender.sendMessage(ChatColor.GREEN + "Новые данные: " + store_id + " " + server + " " + secretKey);
    }
}
