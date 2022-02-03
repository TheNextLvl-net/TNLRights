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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Rights extends TNLPlugin {

    @Nullable
    private static Rights instance = null;

    @Override
    public void enable() {
        setInstance(this);
        Group.loadAll();
        Permissions.loadAll();
        getCommandManager().registerCommand(new RightsCommand());
        getCommandManager().registerCommand(new GroupCommand());
        getEventManager().registerListener(new ConnectionListener());
        for (TNLPlayer all : TNLListener.getOnlinePlayers()) {
            Group group = Group.get(all.bukkit());
            group.updateMember(all.bukkit());
            group.updatePermissions(all.bukkit());
        }
        async(() -> {
            if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
        });
    }

    @Override
    public void disable() {
        Group.exportAll();
        Permissions.exportAll();
    }

    @Nonnull
    public static Rights getInstance() {
        assert instance != null;
        return instance;
    }

    public static void setInstance(@Nonnull Rights instance) {
        Rights.instance = instance;
    }
}
