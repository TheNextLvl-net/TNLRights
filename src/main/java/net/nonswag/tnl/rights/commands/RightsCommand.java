package net.nonswag.tnl.rights.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.rights.api.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RightsCommand extends TNLCommand {

    public RightsCommand() {
        super("rights", "tnl.rights");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length >= 2 && !args[1].isEmpty()) {
                    OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
                    if (arg != null) {
                        if (args.length >= 3) {
                            if (!Permissions.hasPermission(arg, args[2])) {
                                Permissions.addPermission(arg, args[2], source);
                                source.sendMessage("%prefix% §6" + arg.getName() + "§a now has §6" + args[2] + "§a permission");
                            } else source.sendMessage("%prefix% §cNothing could be changed");
                        } else source.sendMessage("%prefix% §c/rights add " + arg.getName() + " §8[§6Permission§8]");
                    } else source.sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
                } else source.sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length >= 2 && !args[1].isEmpty()) {
                    OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
                    if (arg != null) {
                        if (args.length >= 3) {
                            if (Permissions.isPermissionSet(arg, args[2])) {
                                Permissions.unsetPermission(arg, args[2], source);
                                source.sendMessage("%prefix% §6" + arg.getName() + "§a no longer has §6" + args[2] + "§a permission");
                            } else source.sendMessage("%prefix% §cNothing could be changed");
                        } else source.sendMessage("%prefix% §c/rights remove " + arg.getName() + " §8[§6Permission§8]");
                    } else source.sendMessage("%prefix% §c/rights remove §8[§6Player§8] §8[§6Permission§8]");
                } else source.sendMessage("%prefix% §c/rights remove §8[§6Player§8] §8[§6Permission§8]");
            } else if (args[0].equalsIgnoreCase("disallow")) {
                if (args.length >= 2 && !args[1].isEmpty()) {
                    OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
                    if (arg != null) {
                        if (args.length >= 3) {
                            if (!Permissions.isPermissionSet(arg, args[2]) || Permissions.hasPermission(arg, args[2])) {
                                Permissions.removePermission(arg, args[2], source);
                                source.sendMessage("%prefix% §6" + arg.getName() + "§a no longer can use §6" + args[2] + "§a permission");
                            } else source.sendMessage("%prefix% §cNothing could be changed");
                        } else source.sendMessage("%prefix% §c/rights disallow " + arg.getName() + " §8[§6Permission§8]");
                    } else source.sendMessage("%prefix% §c/rights disallow §8[§6Player§8] §8[§6Permission§8]");
                } else source.sendMessage("%prefix% §c/rights disallow §8[§6Player§8] §8[§6Permission§8]");
            } else if (args[0].equalsIgnoreCase("list")) {
                if (args.length >= 2 && !args[1].isEmpty()) {
                    OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
                    if (arg != null) {
                        List<String> allowed = Permissions.getAllowedPermissions(arg);
                        List<String> denied = Permissions.getDeniedPermissions(arg);
                        String amount = arg.isOp() ? "∞" : String.valueOf(allowed.size());
                        source.sendMessage("%prefix%§7 Allowed Permissions §8(§a" + amount + "§8): §6" + String.join("§8, §6", allowed));
                        source.sendMessage("%prefix%§7 Denied Permissions §8(§a" + denied.size() + "§8): §6" + String.join("§8, §6", denied));
                    } else source.sendMessage("%prefix% §c/rights list §8[§6Player§8]");
                } else source.sendMessage("%prefix% §c/rights list §8[§6Player§8]");
            } else help(source);
        } else help(source);
    }

    private void help(@Nonnull CommandSource source) {
        source.sendMessage("%prefix% §c/rights remove §8[§6Player§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/rights disallow §8[§6Player§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
        source.sendMessage("%prefix% §c/rights list §8[§6Player§8]");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("disallow");
            suggestions.add("list");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("disallow")) {
                for (OfflinePlayer all : Bukkit.getOfflinePlayers()) suggestions.add(all.getName());
            } else if (args[0].equalsIgnoreCase("remove")) {
                for (OfflinePlayer all : Bukkit.getOfflinePlayers()) {
                    if (Permissions.hasPermissions(all)) suggestions.add(all.getName());
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                for (OfflinePlayer all : Bukkit.getOfflinePlayers()) {
                    if (!Permissions.hasPermissions(all) && Permissions.getDeniedPermissions(all).isEmpty()) continue;
                    suggestions.add(all.getName());
                }
            }
        } else if (args.length == 3 && !args[1].isEmpty()) {
            if (args[0].equalsIgnoreCase("remove")) {
                OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (arg != null) {
                    suggestions.addAll(Permissions.getDeniedPermissions(arg));
                    suggestions.addAll(Permissions.getAllowedPermissions(arg));
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (arg != null) suggestions.addAll(Permissions.getAllPermissions(arg));
            } else if (args[0].equalsIgnoreCase("disallow")) {
                OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (arg != null) suggestions.addAll(Permissions.getAllowedPermissions(arg));
            }
        }
        return suggestions;
    }
}
