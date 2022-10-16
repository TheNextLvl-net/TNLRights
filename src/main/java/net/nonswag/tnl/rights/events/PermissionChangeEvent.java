package net.nonswag.tnl.rights.events;

import lombok.Getter;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.listener.api.event.TNLEvent;

import javax.annotation.Nonnull;

@Getter
public abstract class PermissionChangeEvent extends TNLEvent {

    @Nonnull
    private final String permission;
    @Nonnull
    private final Type type;
    @Nonnull
    private final CommandSource source;

    protected PermissionChangeEvent(@Nonnull String permission, @Nonnull Type type, @Nonnull CommandSource source) {
        this.permission = permission;
        this.type = type;
        this.source = source;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    public enum Type {
        UNSET, GRANT, REVOKE
    }
}
