package net.nonswag.tnl.rights;

import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.rights.api.Group;
import net.nonswag.tnl.rights.api.Permissions;
import net.nonswag.tnl.rights.commands.GroupCommand;
import net.nonswag.tnl.rights.commands.RightsCommand;
import net.nonswag.tnl.rights.listener.ConnectionListener;

public class Rights extends TNLPlugin {

    @Override
    public void enable() {
        Group.loadAll();
        Permissions.loadAll();
        getCommandManager().registerCommand(new RightsCommand());
        getCommandManager().registerCommand(new GroupCommand());
        getEventManager().registerListener(new ConnectionListener());
    }

    @Override
    public void disable() {
        Group.exportAll();
        Permissions.exportAll();
    }
}
