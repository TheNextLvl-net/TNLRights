package net.nonswag.tnl.rights.listener;

import net.nonswag.tnl.listener.events.TNLPlayerJoinEvent;
import net.nonswag.tnl.rights.api.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(@Nonnull TNLPlayerJoinEvent event) {
        event.getPlayer().getPermissionManager().setPermissions(Permissions.getPermissions(event.getPlayer()));
    }
}
