package net.nonswag.tnl.rights;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.nonswag.core.api.annotation.FieldsAreNullableByDefault;
import net.nonswag.core.api.annotation.MethodsReturnNonnullByDefault;
import net.nonswag.core.api.file.formats.JsonFile;
import net.nonswag.tnl.listener.Listener;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.rights.api.Group;
import net.nonswag.tnl.rights.api.Permissions;
import net.nonswag.tnl.rights.commands.GroupCommand;
import net.nonswag.tnl.rights.commands.RightsCommand;
import net.nonswag.tnl.rights.listener.ConnectionListener;
import net.nonswag.tnl.rights.listener.GroupListener;
import net.nonswag.tnl.rights.listener.PermissionListener;
import net.nonswag.tnl.rights.utils.Messages;
import net.nonswag.tnl.rights.utils.Placeholders;

@Getter
@FieldsAreNullableByDefault
@MethodsReturnNonnullByDefault
public class Rights extends TNLPlugin {
    private static Rights instance = null;
    private boolean safetyFeature = true;

    @Override
    public void enable() {
        instance = this;
        Group.loadAll();
        Permissions.loadAll();
        Placeholders.init();
        Messages.init();
        JsonFile configuration = new JsonFile("plugins/Rights", "config.json");
        JsonObject root = configuration.getJsonElement().getAsJsonObject();
        if (!root.has("safety-feature")) {
            root.addProperty("safety-feature", isSafetyFeature());
            configuration.save();
        } else safetyFeature = root.get("safety-feature").getAsBoolean();
        getCommandManager().registerCommand(new RightsCommand());
        getCommandManager().registerCommand(new GroupCommand());
        getEventManager().registerListener(new ConnectionListener());
        if (isSafetyFeature()) {
            getEventManager().registerListener(new PermissionListener());
            getEventManager().registerListener(new GroupListener());
        }
        for (TNLPlayer all : Listener.getOnlinePlayers()) {
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

    public static Rights getInstance() {
        assert instance != null;
        return instance;
    }
}
