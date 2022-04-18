package net.nonswag.tnl.rights.events.player;

import lombok.Getter;
import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

@Getter
public class PlayerPermissionChangeEvent extends PermissionChangeEvent {

    @Nonnull
    private final UUID player;

    public PlayerPermissionChangeEvent(@Nonnull UUID player, @Nonnull String permission, @Nonnull Type type, @Nonnull CommandSource source) {
        super(permission, type, source);
        this.player = player;
    }
}
