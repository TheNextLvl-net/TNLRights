package net.nonswag.tnl.rights.events.group;

import net.nonswag.tnl.rights.api.Group;

import javax.annotation.Nonnull;
import java.util.UUID;

public class GroupMemberRemoveEvent extends GroupMemberEvent {

    public GroupMemberRemoveEvent(@Nonnull Group group, @Nonnull UUID player) {
        super(group, player);
    }
}
