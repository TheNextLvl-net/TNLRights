package net.nonswag.tnl.rights.commands.rights;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.UnknownPlayerException;
import net.nonswag.tnl.listener.api.command.simple.SubCommand;
import net.nonswag.tnl.rights.api.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

class Add extends SubCommand {

    Add() {
        super("add");
    }

    @Override
    protected void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 2 || args[1].isEmpty()) throw new InvalidUseException(this);
        OfflinePlayer arg = Bukkit.getOfflinePlayerIfCached(args[1]);
        if (arg == null) throw new UnknownPlayerException(args[1]);
        if (args.length >= 3 && !Permissions.hasPermission(arg, args[2])) {
            Permissions.addPermission(arg, args[2], source);
            source.sendMessage("%prefix% §6" + arg.getName() + "§a now has §6" + args[2] + "§a permission");
        } else if (args.length >= 3) source.sendMessage("%prefix% §cNothing could be changed");
        else source.sendMessage("%prefix% §c/rights add " + arg.getName() + " §8[§6Permission§8]");
    }

    @Override
    public void usage(Invocation invocation) {
        invocation.source().sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
    }
}
