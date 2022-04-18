package net.nonswag.tnl.rights.events.group;

import lombok.Getter;
import net.nonswag.tnl.listener.api.event.TNLEvent;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.api.Group;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Getter
abstract class GroupMemberEvent extends TNLEvent {

    @Nonnull
    private final Group group;
    @Nonnull
    private final UUID player;

    GroupMemberEvent(@Nonnull Group group, @Nonnull UUID player) {
        this.group = group;
        this.player = player;
    }

    @Override
    protected boolean denyCancellation() {
        return true;
    }

    @Nullable
    public TNLPlayer getOnlinePlayer() {
        return TNLPlayer.cast(getPlayer());
    }
}
