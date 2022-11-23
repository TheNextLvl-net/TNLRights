package net.nonswag.tnl.rights.listener;

import net.nonswag.core.api.logger.Console;
import net.nonswag.core.api.platform.PlatformPlayer;
import net.nonswag.tnl.listener.Listener;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;
import net.nonswag.tnl.rights.events.group.GroupPermissionChangeEvent;
import net.nonswag.tnl.rights.events.player.PlayerPermissionChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PermissionListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onGroupPermissionChange(GroupPermissionChangeEvent event) {
        notifyChange(event, event.getGroup().getName());
    }

    @EventHandler
    public void onPlayerPermissionChange(PlayerPermissionChangeEvent event) {
        String name = Bukkit.getOfflinePlayer(event.getPlayer()).getName();
        notifyChange(event, name == null ? event.getPlayer().toString() : name);
    }

    private void notifyChange(PermissionChangeEvent event, String target) {
        String source = event.getSource() instanceof PlatformPlayer player ? player.getName() : "Console";
        String type = event.getType().name().toLowerCase();
        for (TNLPlayer all : Listener.getOnlinePlayers()) {
            if (!all.permissionManager().hasPermission("tnl.rights")) continue;
            if (all.equals(event.getSource())) continue;
            all.messenger().sendMessage("%prefix% §7" + source + "§8: §6" + target + " §8- §6" +
                    event.getPermission() + " §8(§a" + event.getType().name().toLowerCase() + "§8)");
        }
        if (event.getSource() instanceof Console) return;
        event.getSource().sendMessage("%prefix% §7" + source + "§8: §6" + target + " §8- §6" +
                event.getPermission() + " §8(§a" + event.getType().name().toLowerCase() + "§8)");
    }
}
