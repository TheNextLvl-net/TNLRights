package net.nonswag.tnl.rights.events;

import lombok.Getter;
import net.nonswag.tnl.listener.api.event.TNLEvent;

import javax.annotation.Nonnull;

@Getter
public abstract class PermissionChangeEvent extends TNLEvent {

    @Nonnull
    private final String permission;
    @Nonnull
    private final Type type;

    protected PermissionChangeEvent(@Nonnull String permission, @Nonnull Type type) {
        this.permission = permission;
        this.type = type;
    }

    @Override
    protected boolean denyCancellation() {
        return true;
    }

    public enum Type {
        UNSET, GRANT, REVOKE
    }
}
