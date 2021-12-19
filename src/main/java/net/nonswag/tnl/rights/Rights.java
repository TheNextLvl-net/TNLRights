package net.nonswag.tnl.rights;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.listener.api.settings.Settings;
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
        for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) {
            Group group = Group.get(all);
            group.updateMember(all);
            group.updatePermissions(all);
        }
        if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
    }

    @Override
    public void disable() {
        Group.exportAll();
        Permissions.exportAll();
    }
}
