package net.nonswag.tnl.rights.events.group;

import lombok.Getter;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.rights.api.Group;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;

import javax.annotation.Nonnull;

@Getter
public class GroupPermissionChangeEvent extends PermissionChangeEvent {

    @Nonnull
    private final Group group;

    public GroupPermissionChangeEvent(@Nonnull Group group, @Nonnull String permission, @Nonnull Type type, @Nonnull CommandSource source) {
        super(permission, type, source);
        this.group = group;
    }
}
