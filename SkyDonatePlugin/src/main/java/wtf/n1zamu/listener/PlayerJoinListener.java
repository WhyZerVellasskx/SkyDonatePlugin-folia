package wtf.n1zamu.listener;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.n1zamu.SkyDonatePlugin;

public class PlayerJoinListener implements Listener {
    private final SkyDonatePlugin plugin;
    private final LuckPerms luckPerms;

    public PlayerJoinListener(SkyDonatePlugin plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (luckPerms == null) {
            return;
        }
        User user = luckPerms.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        user.getNodes().forEach(node -> {
            if (node.hasExpiry()) {
                return;
            }
            if (!plugin.getDefaultConfiguration().getGroupId().containsKey(node.getKey())) {
                return;
            }
            plugin.getUpdateTask().getUpdateCache().put(event.getPlayer().getName(), plugin.getDefaultConfiguration().getGroupId().get(node.getKey()));
        });
    }
}
