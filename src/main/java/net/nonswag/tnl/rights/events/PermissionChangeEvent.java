package net.nonswag.tnl.rights.events;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.listener.api.event.TNLEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PermissionChangeEvent extends TNLEvent {
    private final String permission;
    private final Type type;
    private final CommandSource source;

    public enum Type {
        UNSET, GRANT, REVOKE
    }
}
