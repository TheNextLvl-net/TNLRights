package net.nonswag.tnl.rights.events.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.rights.api.Group;

import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class GroupMemberEvent extends TNLEvent {
    private final Group group;
    private final UUID player;
    private final CommandSource source;
}
