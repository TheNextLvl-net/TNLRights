package net.nonswag.tnl.rights;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.rights.api.Permissions;
import net.nonswag.tnl.rights.commands.RightsCommand;
import net.nonswag.tnl.rights.listener.ConnectionListener;

public class Rights extends TNLPlugin {

    @Override
    public void onEnable() {
        CommandManager.registerCommands(new RightsCommand());
        EventManager eventManager = EventManager.cast(this);
        eventManager.registerListener(new ConnectionListener());
        Permissions.getInstance().loadAll();
    }

    @Override
    public void onDisable() {
        Permissions.getInstance().exportAll();
    }
}
