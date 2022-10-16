package net.nonswag.tnl.rights.events.group;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.rights.api.Group;

import javax.annotation.Nonnull;
import java.util.UUID;

public class GroupMemberAddEvent extends GroupMemberEvent {

    public GroupMemberAddEvent(@Nonnull Group group, @Nonnull UUID player, @Nonnull CommandSource source) {
        super(group, player, source);
    }
}
