package net.nonswag.tnl.rights.events.player;

import lombok.Getter;
import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@Getter
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerPermissionChangeEvent extends PermissionChangeEvent {
    private final UUID player;

    public PlayerPermissionChangeEvent(UUID player, String permission, Type type, CommandSource source) {
        super(permission, type, source);
        this.player = player;
    }
}
