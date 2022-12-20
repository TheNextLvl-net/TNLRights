package net.nonswag.tnl.rights.events.group;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.rights.api.group.Group;

import java.util.UUID;

public class GroupMemberRemoveEvent extends GroupMemberEvent {
    public GroupMemberRemoveEvent(Group group, UUID player, CommandSource source) {
        super(group, player, source);
    }
}
