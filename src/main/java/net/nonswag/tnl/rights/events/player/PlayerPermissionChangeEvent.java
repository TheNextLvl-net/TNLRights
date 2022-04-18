package net.nonswag.tnl.rights.events.player;

import lombok.Getter;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Getter
public class PlayerPermissionChangeEvent extends PermissionChangeEvent {

    @Nonnull
    private final UUID player;

    public PlayerPermissionChangeEvent(@Nonnull UUID player, @Nonnull String permission, @Nonnull Type type) {
        super(permission, type);
        this.player = player;
    }

    @Nullable
    public TNLPlayer getOnlinePlayer() {
        return TNLPlayer.cast(getPlayer());
    }
}
