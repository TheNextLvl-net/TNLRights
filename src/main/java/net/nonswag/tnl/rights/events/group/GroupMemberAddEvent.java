package net.nonswag.tnl.rights.events.group;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.rights.api.Group;

import java.util.UUID;

public class GroupMemberAddEvent extends GroupMemberEvent {
    public GroupMemberAddEvent(Group group, UUID player, CommandSource source) {
        super(group, player, source);
    }
}
