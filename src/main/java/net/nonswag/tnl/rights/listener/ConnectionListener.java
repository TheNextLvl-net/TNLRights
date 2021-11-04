package net.nonswag.tnl.rights.listener;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.api.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        TNLPlayer player = TNLPlayer.cast(event.getPlayer());
        player.getPermissionManager().setPermissions(Permissions.getInstance().getPermissions(player));
    }
}
