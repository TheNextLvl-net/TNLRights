package net.nonswag.tnl.rights.listener;

import net.nonswag.core.api.message.Placeholder;
import net.nonswag.core.api.message.key.MessageKey;
import net.nonswag.tnl.listener.Listener;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.events.group.GroupMemberAddEvent;
import net.nonswag.tnl.rights.events.group.GroupMemberEvent;
import net.nonswag.tnl.rights.events.group.GroupMemberRemoveEvent;
import net.nonswag.tnl.rights.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class GroupListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onGroupMemberAdd(@Nonnull GroupMemberAddEvent event) {
        String name = Bukkit.getOfflinePlayer(event.getPlayer()).getName();
        notifyChange(event, Messages.MEMBER_ADDED, name == null ? event.getPlayer().toString() : name);
    }

    @EventHandler
    public void onGroupMemberRemove(@Nonnull GroupMemberRemoveEvent event) {
        String name = Bukkit.getOfflinePlayer(event.getPlayer()).getName();
        notifyChange(event, Messages.MEMBER_REMOVED, name == null ? event.getPlayer().toString() : name);
    }

    private void notifyChange(@Nonnull GroupMemberEvent event, @Nonnull MessageKey key, @Nonnull String target) {
        String source = event.getSource().isConsole() ? "Console" : event.getSource().player().getName();
        for (TNLPlayer all : Listener.getOnlinePlayers()) {
            if (!all.permissionManager().hasPermission("tnl.rights")) continue;
            if (all.equals(event.getSource())) continue;
            all.messenger().sendMessage(key, new Placeholder("source", source), new Placeholder("member", target), new Placeholder("group", event.getGroup().getName()));
        }
        if (!event.getSource().isConsole()) {
            event.getSource().console().sendMessage(key, new Placeholder("source", source), new Placeholder("member", target), new Placeholder("group", event.getGroup().getName()));
        }
    }
}
