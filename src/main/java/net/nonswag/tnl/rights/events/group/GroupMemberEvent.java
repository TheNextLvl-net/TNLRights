package net.nonswag.tnl.rights.events.group;

import lombok.Getter;
import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.rights.api.Group;

import javax.annotation.Nonnull;
import java.util.UUID;

@Getter
public abstract class GroupMemberEvent extends TNLEvent {

    @Nonnull
    private final Group group;
    @Nonnull
    private final UUID player;
    @Nonnull
    private final CommandSource source;

    protected GroupMemberEvent(@Nonnull Group group, @Nonnull UUID player, @Nonnull CommandSource source) {
        this.group = group;
        this.player = player;
        this.source = source;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
