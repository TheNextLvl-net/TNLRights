package net.nonswag.tnl.rights.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
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
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("members")) {
                source.sendMessage("%prefix% §cSoon");
            } else if (args[0].equalsIgnoreCase("rights")) {
                source.sendMessage("%prefix% §cSoon");
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length >= 2) {
                    Group group = Group.get(args[1]);
                    if (group != null) {
                        if (group.delete()) source.sendMessage("%prefix% §aDeleted group §6" + group.getName());
                        else source.sendMessage("%prefix% §cFailed to delete group §4" + group.getName());
                    } else source.sendMessage("%prefix% §c/group delete §8[§6Group§8]");
                } else source.sendMessage("%prefix% §c/group delete §8[§6Group§8]");
            } else if (args[0].equalsIgnoreCase("create")) {
                if (args.length >= 2) {
                    Group group = Group.get(args[1]);
                    if (group == null) {
                        group = new Group(args[1]).register();
                        source.sendMessage("%prefix% §aCreated group §6" + group.getName());
                    } else source.sendMessage("%prefix% §cThe name §4" + group.getName() + "§c is already taken");
                } else source.sendMessage("%prefix% §c/group create §8[§6Name§8]");
            } else if (args[0].equalsIgnoreCase("list")) {
                List<String> groups = new ArrayList<>();
                for (Group group : Group.getGroups()) groups.add(group.getName());
                source.sendMessage("%prefix% §7Groups §8(§a" + groups.size() + "§8): §6" + String.join("§8, §6", groups));
            } else help(source);
        } else help(source);
    }

    private void help(@Nonnull CommandSource source) {
        source.sendMessage("%prefix% §c/group members remove §8[§6Group§8] §8[§6Member§8]");
        source.sendMessage("%prefix% §c/group members add §8[§6Group§8] §8[§6Player§8]");
        source.sendMessage("%prefix% §c/group rights disallow §8[§6Group§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/group rights remove §8[§6Group§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/group rights add §8[§6Group§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/group rights list §8[§6Group§8]");
        source.sendMessage("%prefix% §c/group delete §8[§6Group§8]");
        source.sendMessage("%prefix% §c/group create §8[§6Name§8]");
        source.sendMessage("%prefix% §c/group list");
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
            suggestions.add("members");
            suggestions.add("rights");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                for (Group group : Group.getGroups()) {
                    if (!group.equals(Group.DEFAULT)) suggestions.add(group.getName());
                }
            } else if (args[0].equalsIgnoreCase("members")) {
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
            if (args[0].equalsIgnoreCase("members")) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("list")) {
                    for (Group group : Group.getGroups()) suggestions.add(group.getName());
                }
            } else if (args[0].equalsIgnoreCase("rights")) {
                if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("disallow") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("add")) {
                    for (Group group : Group.getGroups()) suggestions.add(group.getName());
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("members")) {
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
