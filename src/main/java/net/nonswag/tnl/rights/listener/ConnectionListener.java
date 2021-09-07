package net.nonswag.tnl.rights.listener;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.api.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.List;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        TNLPlayer player = TNLPlayer.cast(event.getPlayer());
        List<String> permissions = Permissions.getInstance().getPermissions(player);
        player.getPermissionManager().addPermission(permissions.toArray(new String[]{}));
    }

    @EventHandler
    public void onQuit(@Nonnull PlayerQuitEvent event) {
        TNLPlayer player = TNLPlayer.cast(event.getPlayer());
        Permissions.getInstance().setPermissions(player, player.getPermissionManager().getPermissions());
    }
}
