package net.nonswag.tnl.rights.listener;

import net.nonswag.tnl.listener.events.TNLPlayerJoinEvent;
import net.nonswag.tnl.rights.api.group.Group;
import net.nonswag.tnl.rights.api.permissions.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(TNLPlayerJoinEvent event) {
        Permissions.update(event.getPlayer());
        Group.get(event.getPlayer()).updateMember(event.getPlayer().getUniqueId());
    }
}
