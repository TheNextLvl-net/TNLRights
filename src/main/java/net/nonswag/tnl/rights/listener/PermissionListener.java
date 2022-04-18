package net.nonswag.tnl.rights.listener;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;
import net.nonswag.tnl.rights.events.group.GroupPermissionChangeEvent;
import net.nonswag.tnl.rights.events.player.PlayerPermissionChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

public class PermissionListener implements Listener {

    @EventHandler
    public void onGroupPermissionChange(@Nonnull GroupPermissionChangeEvent event) {
        notifyChange(event, event.getGroup().getName());
    }

    @EventHandler
    public void onPlayerPermissionChange(@Nonnull PlayerPermissionChangeEvent event) {
        String name = Bukkit.getOfflinePlayer(event.getPlayer()).getName();
        notifyChange(event, name == null ? event.getPlayer().toString() : name);
    }

    private void notifyChange(@Nonnull PermissionChangeEvent event, @Nonnull String target) {
        String source = event.getSource().isConsole() ? "Console" : event.getSource().player().getName();
        String type = event.getType().name().toLowerCase();
        for (TNLPlayer all : TNLListener.getOnlinePlayers()) {
            if (!all.permissionManager().hasPermission("tnl.rights")) continue;
            if (all.equals(event.getSource())) continue;
            all.messenger().sendMessage("%prefix% §7" + source + "§8: §6" + target + " §8- §6"+
                    event.getPermission() + " §8(§a" + event.getType().name().toLowerCase() + "§8)");
        }
    }
}
