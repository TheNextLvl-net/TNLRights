package net.nonswag.tnl.rights;

import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.rights.api.Permissions;
import net.nonswag.tnl.rights.commands.RightsCommand;
import net.nonswag.tnl.rights.listener.ConnectionListener;

public class Rights extends TNLPlugin {

    @Override
    public void onEnable() {
        getCommandManager().registerCommands(new RightsCommand());
        getEventManager().registerListener(new ConnectionListener());
        Permissions.getInstance().loadAll();
    }

    @Override
    public void onDisable() {
        Permissions.getInstance().exportAll();
    }
}
