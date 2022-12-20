package net.nonswag.tnl.rights;

import net.nonswag.core.api.annotation.FieldsAreNullableByDefault;
import net.nonswag.core.api.annotation.MethodsReturnNonnullByDefault;
import net.nonswag.tnl.listener.Listener;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.rights.api.file.Config;
import net.nonswag.tnl.rights.api.group.Group;
import net.nonswag.tnl.rights.api.permissions.Permissions;
import net.nonswag.tnl.rights.commands.GroupCommand;
import net.nonswag.tnl.rights.commands.RightsCommand;
import net.nonswag.tnl.rights.listener.ConnectionListener;
import net.nonswag.tnl.rights.listener.GroupListener;
import net.nonswag.tnl.rights.listener.PermissionListener;
import net.nonswag.tnl.rights.utils.Messages;
import net.nonswag.tnl.rights.utils.Placeholders;

@FieldsAreNullableByDefault
@MethodsReturnNonnullByDefault
public class Rights extends TNLPlugin {
    private static Rights instance = null;

    @Override
    public void enable() {
        instance = this;
        Group.loadAll();
        Permissions.loadAll();
        Placeholders.init();
        Messages.init();
        registerListeners();
        Listener.getOnlinePlayers().forEach(all -> Group.get(all).updateMember(all));
        async(() -> {
            if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
        });
    }

    @Override
    public void disable() {
        Config.getInstance().export();
        Group.exportAll();
        Permissions.exportAll();
    }

    private void registerListeners() {
        getCommandManager().registerCommand(new RightsCommand());
        getCommandManager().registerCommand(new GroupCommand());
        getEventManager().registerListener(new ConnectionListener());
        if (!Config.getInstance().safetyFeature()) return;
        getEventManager().registerListener(new PermissionListener());
        getEventManager().registerListener(new GroupListener());
    }

    public static Rights getInstance() {
        assert instance != null;
        return instance;
    }
}
