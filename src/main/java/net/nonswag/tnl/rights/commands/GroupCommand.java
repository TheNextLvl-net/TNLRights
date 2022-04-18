package net.nonswag.tnl.rights.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.logger.Color;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.rights.api.Group;
import net.nonswag.tnl.rights.api.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
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
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length >= 3) {
                            Group group = Group.get(args[2]);
                            if (group != null) {
                                if (args.length >= 4) {
                                    OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args[3]);
                                    if (player != null) {
                                        if (!group.isDefault() && group.isMember(player)) {
                                            group.removePlayer(player, source);
                                            source.sendMessage("%prefix% §aRemoved §6" + player.getName() + "§a from group §6" + group.getName());
                                        } else source.sendMessage("%prefix% §cNothing could be changed");
                                    } else {
                                        source.sendMessage("%prefix% §c/group members remove " + group.getName() + " §8[§6Member§8]");
                                    }
                                } else {
                                    source.sendMessage("%prefix% §c/group members remove " + group.getName() + " §8[§6Member§8]");
                                }
                            } else source.sendMessage("%prefix% §c/group members remove §8[§6Group§8] §8[§6Member§8]");
                        } else source.sendMessage("%prefix% §c/group members remove §8[§6Group§8] §8[§6Member§8]");
                    } else if (args[1].equalsIgnoreCase("add")) {
                        if (args.length >= 3) {
                            Group group = Group.get(args[2]);
                            if (group != null) {
                                if (args.length >= 4) {
                                    OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args[3]);
                                    if (player != null) {
                                        if (group.isDefault() || !group.isMember(player)) {
                                            group.addPlayer(player, source);
                                            source.sendMessage("%prefix% §aAdded §6" + player.getName() + "§a to group §6" + group.getName());
                                        } else source.sendMessage("%prefix% §cNothing could be changed");
                                    } else {
                                        source.sendMessage("%prefix% §c/group members add " + group.getName() + " §8[§6Player§8]");
                                    }
                                } else {
                                    source.sendMessage("%prefix% §c/group members add " + group.getName() + " §8[§6Player§8]");
                                }
                            } else source.sendMessage("%prefix% §c/group members add §8[§6Group§8] §8[§6Player§8]");
                        } else source.sendMessage("%prefix% §c/group members add §8[§6Group§8] §8[§6Player§8]");
                    } else if (args[1].equalsIgnoreCase("list")) {
                        if (args.length >= 3) {
                            Group group = Group.get(args[2]);
                            if (group != null) {
                                List<String> members = new ArrayList<>();
                                for (UUID member : group.getMembers()) {
                                    String name = Bukkit.getOfflinePlayer(member).getName();
                                    if (name != null) members.add(name);
                                }
                                source.sendMessage("%prefix% §7Members §8(§a" + members.size() + "§8): §6" + String.join("§8, §6", members));
                            } else source.sendMessage("%prefix% §c/group members list §8[§6Group§8]");
                        } else source.sendMessage("%prefix% §c/group members list §8[§6Group§8]");
                    } else {
                        source.sendMessage("%prefix% §c/group members remove §8[§6Group§8] §8[§6Member§8]");
                        source.sendMessage("%prefix% §c/group members add §8[§6Group§8] §8[§6Player§8]");
                        source.sendMessage("%prefix% §c/group members list §8[§6Group§8]");
                    }
                } else {
                    source.sendMessage("%prefix% §c/group members remove §8[§6Group§8] §8[§6Member§8]");
                    source.sendMessage("%prefix% §c/group members add §8[§6Group§8] §8[§6Player§8]");
                    source.sendMessage("%prefix% §c/group members list §8[§6Group§8]");
                }
            } else if (args[0].equalsIgnoreCase("rights")) {
                source.sendMessage("%prefix% §cGroup rights are not available at the moment");
            } else if (args[0].equalsIgnoreCase("prefix")) {
                if (args.length >= 2) {
                    Group group = Group.get(args[1]);
                    if (group != null) {
                        if (args.length >= 3) {
                            String prefix = Color.colorize(String.join(" ", Arrays.asList(args).subList(2, args.length)), '&');
                            if (prefix.length() >= 64) prefix = prefix.substring(64);
                            group.getTeam().setPrefix(prefix);
                            group.updatePlayers();
                            source.sendMessage("%prefix% §aChanged prefix to §6" + prefix);
                        } else source.sendMessage("%prefix% §c/group prefix " + group.getName() + " §8[§6Prefix§8]");
                    } else source.sendMessage("%prefix% §c/group prefix §8[§6Group§8] §8[§6Prefix§8]");
                } else source.sendMessage("%prefix% §c/group prefix §8[§6Group§8] §8[§6Prefix§8]");
            } else if (args[0].equalsIgnoreCase("suffix")) {
                if (args.length >= 2) {
                    Group group = Group.get(args[1]);
                    if (group != null) {
                        if (args.length >= 3) {
                            String suffix = Color.colorize(String.join(" ", Arrays.asList(args).subList(2, args.length)), '&');
                            if (suffix.length() >= 64) suffix = suffix.substring(64);
                            group.getTeam().setSuffix(suffix);
                            group.updatePlayers();
                            source.sendMessage("%prefix% §aChanged suffix to §6" + suffix);
                        } else source.sendMessage("%prefix% §c/group suffix " + group.getName() + " §8[§6Suffix§8]");
                    } else source.sendMessage("%prefix% §c/group suffix §8[§6Group§8] §8[§6Suffix§8]");
                } else source.sendMessage("%prefix% §c/group suffix §8[§6Group§8] §8[§6Suffix§8]");
            } else if (args[0].equalsIgnoreCase("color")) {
                if (args.length >= 2) {
                    Group group = Group.get(args[1]);
                    if (group != null) {
                        if (args.length >= 3) {
                            try {
                                ChatColor color = ChatColor.valueOf(args[2].toUpperCase());
                                group.getTeam().setColor(color);
                                group.updatePlayers();
                                source.sendMessage("%prefix% §aChanged color to §6" + color + color.name().toLowerCase().replace("_", " "));
                            } catch (IllegalArgumentException e) {
                                source.sendMessage("%prefix% §c/group color " + group.getName() + " §8[§6Color§8]");
                            }
                        } else source.sendMessage("%prefix% §c/group color " + group.getName() + " §8[§6Color§8]");
                    } else source.sendMessage("%prefix% §c/group color §8[§6Group§8] §8[§6Color§8]");
                } else source.sendMessage("%prefix% §c/group color §8[§6Group§8] §8[§6Color§8]");
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
        source.sendMessage("%prefix% §c/group members list §8[§6Group§8]");
        source.sendMessage("%prefix% §c/group rights disallow §8[§6Group§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/group rights remove §8[§6Group§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/group rights add §8[§6Group§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/group rights list §8[§6Group§8]");
        source.sendMessage("%prefix% §c/group prefix §8[§6Group§8] §8[§6Prefix§8]");
        source.sendMessage("%prefix% §c/group suffix §8[§6Group§8] §8[§6Suffix§8]");
        source.sendMessage("%prefix% §c/group color §8[§6Group§8] §8[§6Color§8]");
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
            suggestions.add("prefix");
            suggestions.add("suffix");
            suggestions.add("color");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                for (Group group : Group.getGroups()) {
                    if (!group.equals(Group.DEFAULT)) suggestions.add(group.getName());
                }
            } else if (args[0].equalsIgnoreCase("prefix") || args[0].equalsIgnoreCase("suffix") || args[0].equalsIgnoreCase("color")) {
                for (Group group : Group.getGroups()) suggestions.add(group.getName());
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
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("list")) {
                    for (Group group : Group.getGroups()) suggestions.add(group.getName());
                } else if (args[1].equalsIgnoreCase("remove")) {
                    for (Group group : Group.getGroups()) if (!group.isDefault()) suggestions.add(group.getName());
                }
            } else if (args[0].equalsIgnoreCase("color")) {
                for (ChatColor color : ChatColor.values()) suggestions.add(color.name());
            } else if (args[0].equalsIgnoreCase("rights")) {
                if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("disallow") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("add")) {
                    for (Group group : Group.getGroups()) suggestions.add(group.getName());
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("members")) {
                if (args[1].equalsIgnoreCase("add")) {
                    Group group = Group.get(args[2]);
                    if (group != null) for (OfflinePlayer all : Bukkit.getOfflinePlayers()) {
                        if (!group.isMember(all) && all.getName() != null) suggestions.add(all.getName());
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    Group group = Group.get(args[2]);
                    if (group != null && !group.isDefault()) for (UUID member : group.getMembers()) {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(member);
                        if (player.getName() != null) suggestions.add(player.getName());
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
