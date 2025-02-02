package wtf.n1zamu.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import wtf.n1zamu.api.dto.Purchase;

@AllArgsConstructor

public class PurchaseEvent extends Event {
    private final HandlerList handlers = new HandlerList();
    @Getter
    private final Purchase purchase;

    @Override
    public @NotNull HandlerList getHandlers() {
        return this.handlers;
    }
}
