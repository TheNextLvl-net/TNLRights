package net.nonswag.tnl.rights.commands;

import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.rights.api.Group;
import net.nonswag.tnl.rights.api.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupCommand extends TNLCommand {

    public GroupCommand() {
        super("group", "tnl.rights");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        /*
        group list (group)
        group create [name]
        group delete [group]

        group players add [group] [player]
        group players remove [group] [player]

        group rights list [group]
        group rights disallow [group] [permission]
        group rights remove [group] [permission]
        group rights add [group] [permission]
         */
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            suggestions.add("list");
            suggestions.add("create");
            suggestions.add("delete");
            suggestions.add("players");
            suggestions.add("rights");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("list")) {
                for (Group group : Group.getGroups()) suggestions.add(group.getName());
            } else if (args[0].equalsIgnoreCase("players")) {
                suggestions.add("add");
                suggestions.add("remove");
                suggestions.add("list");
            } else if (args[0].equalsIgnoreCase("rights")) {
                suggestions.add("list");
                suggestions.add("disallow");
                suggestions.add("remove");
                suggestions.add("add");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("players")) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("list")) {
                    for (Group group : Group.getGroups()) suggestions.add(group.getName());
                }
            } else if (args[0].equalsIgnoreCase("rights")) {
                if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("disallow") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("add")) {
                    for (Group group : Group.getGroups()) suggestions.add(group.getName());
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("players")) {
                if (args[1].equalsIgnoreCase("add")) {
                    Group group = Group.get(args[2]);
                    if (group != null) {
                        for (OfflinePlayer all : Bukkit.getOfflinePlayers()) {
                            if (!group.isMember(all) && all.getName() != null) suggestions.add(all.getName());
                        }
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    Group group = Group.get(args[2]);
                    if (group != null) {
                        for (UUID member : group.getMembers()) {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(member);
                            if (player.getName() != null) suggestions.add(player.getName());
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("rights")) {
                if (args[1].equalsIgnoreCase("disallow")) {
                    Group group = Group.get(args[2]);
                    if (group != null) {
                        for (String permission : Permissions.getAllPermissions()) {
                            if (group.hasPermission(permission)) suggestions.add(permission);
                        }
                        for (String permission : Group.getAllPermissions()) {
                            if (group.hasPermission(permission)) suggestions.add(permission);
                        }
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    Group group = Group.get(args[2]);
                    if (group != null) {
                        for (String permission : Permissions.getAllPermissions()) {
                            if (group.isPermissionSet(permission)) suggestions.add(permission);
                        }
                        for (String permission : Group.getAllPermissions()) {
                            if (group.isPermissionSet(permission)) suggestions.add(permission);
                        }
                    }
                } else if (args[1].equalsIgnoreCase("add")) {
                    Group group = Group.get(args[2]);
                    if (group != null) {
                        for (String permission : Permissions.getAllPermissions()) {
                            if (!group.hasPermission(permission)) suggestions.add(permission);
                        }
                        for (String permission : Group.getAllPermissions()) {
                            if (!group.hasPermission(permission)) suggestions.add(permission);
                        }
                    }
                }
            }
        }
        return suggestions;
    }
}
