package net.nonswag.tnl.rights.events.group;

import lombok.Getter;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.rights.api.Group;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;

@Getter
public class GroupPermissionChangeEvent extends PermissionChangeEvent {
    private final Group group;

    public GroupPermissionChangeEvent(Group group, String permission, Type type, CommandSource source) {
        super(permission, type, source);
        this.group = group;
    }
}
