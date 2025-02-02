package wtf.n1zamu.listener;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeMutateEvent;

import wtf.n1zamu.SkyDonatePlugin;

public class NodeMutateListener {
    private final SkyDonatePlugin plugin;

    public NodeMutateListener(SkyDonatePlugin plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(plugin, NodeMutateEvent.class, this::onNodeMutate);
    }

    private void onNodeMutate(NodeMutateEvent event) {

        event.getDataAfter().forEach(node -> {
            if (node.hasExpiry()) {
                return;
            }
            if (!plugin.getDefaultConfiguration().getGroupId().containsKey(node.getKey())) {
                return;
            }
            plugin.getUpdateTask().getUpdateCache().put(event.getTarget().getFriendlyName(), plugin.getDefaultConfiguration().getGroupId().get(node.getKey()));
        });
    }
}
