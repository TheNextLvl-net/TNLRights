package net.nonswag.tnl.rights.api.permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.annotation.MethodsReturnNonnullByDefault;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.file.formats.JsonFile;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.api.group.Group;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;
import net.nonswag.tnl.rights.events.player.PlayerPermissionChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Permissions {
    private static final Map<UUID, Map<String, Boolean>> permissions = new HashMap<>();
    private static final JsonFile FILE = new JsonFile("plugins/Rights", "permissions.json");

    public static void update(UUID uuid) {
        TNLPlayer player = TNLPlayer.cast(uuid);
        if (player != null) update(player);
    }

    public static void update(TNLPlayer player) {
        player.permissionManager().setPermissions(getPermissions(player));
    }

    public static Map<String, Boolean> getPermissions(TNLPlayer player) {
        return getPermissions(player.getUniqueId());
    }

    public static Map<String, Boolean> getPermissions(OfflinePlayer player) {
        return getPermissions(player.getUniqueId());
    }

    public static Map<String, Boolean> getPermissions(UUID uuid) {
        Map<String, Boolean> permissions = new HashMap<>(Group.get(uuid).getPermissions());
        permissions.putAll(permissions(uuid));
        return permissions;
    }

    private static Map<String, Boolean> permissions(UUID uuid) {
        if (!permissions.containsKey(uuid)) permissions.put(uuid, new HashMap<>());
        return permissions.get(uuid);
    }

    public static List<String> getAllowedPermissions(OfflinePlayer player) {
        return getAllowedPermissions(player.getUniqueId());
    }

    public static List<String> getAllowedPermissions(UUID uuid) {
        List<String> permissions = new ArrayList<>();
        permissions(uuid).forEach((permission, allowed) -> {
            if (allowed) permissions.add(permission);
        });
        return permissions;
    }

    public static List<String> getDeniedPermissions(OfflinePlayer player) {
        return getDeniedPermissions(player.getUniqueId());
    }

    public static List<String> getDeniedPermissions(UUID uuid) {
        List<String> permissions = new ArrayList<>();
        permissions(uuid).forEach((permission, allowed) -> {
            if (!allowed) permissions.add(permission);
        });
        return permissions;
    }

    public static void setPermissions(OfflinePlayer player, Map<String, Boolean> permissions) {
        setPermissions(player.getUniqueId(), permissions);
    }

    public static void setPermissions(UUID uuid, Map<String, Boolean> permissions) {
        Permissions.permissions.put(uuid, permissions);
        TNLPlayer player = TNLPlayer.cast(uuid);
        if (player != null) update(player);
    }

    public static boolean hasPermissions(OfflinePlayer player) {
        return hasPermissions(player.getUniqueId());
    }

    public static boolean hasPermissions(UUID uuid) {
        return permissions.containsKey(uuid) && !getPermissions(uuid).isEmpty();
    }

    public static boolean hasPermission(OfflinePlayer player, String permission) {
        return hasPermission(player.getUniqueId(), permission);
    }

    public static boolean hasPermission(UUID uuid, String permission) {
        return getPermissions(uuid).getOrDefault(permission, false);
    }

    public static boolean isPermissionSet(OfflinePlayer player, String permission) {
        return isPermissionSet(player.getUniqueId(), permission);
    }

    public static boolean isPermissionSet(UUID uuid, String permission) {
        return getPermissions(uuid).containsKey(permission);
    }

    public static void addPermission(OfflinePlayer player, String permission, @Nullable CommandSource source) {
        addPermission(player.getUniqueId(), permission, source);
    }

    public static void addPermission(UUID uuid, String permission, @Nullable CommandSource source) {
        permissions(uuid).put(permission, true);
        if (source != null) {
            new PlayerPermissionChangeEvent(uuid, permission, PermissionChangeEvent.Type.GRANT, source).call();
        }
        TNLPlayer player = TNLPlayer.cast(uuid);
        if (player != null) update(player);
    }

    public static void removePermission(OfflinePlayer player, String permission, @Nullable CommandSource source) {
        removePermission(player.getUniqueId(), permission, source);
    }

    public static void removePermission(UUID uuid, String permission, @Nullable CommandSource source) {
        permissions(uuid).put(permission, false);
        if (source != null) {
            new PlayerPermissionChangeEvent(uuid, permission, PermissionChangeEvent.Type.REVOKE, source).call();
        }
        TNLPlayer player = TNLPlayer.cast(uuid);
        if (player != null) update(player);
    }

    public static void unsetPermission(OfflinePlayer player, String permission, @Nullable CommandSource source) {
        unsetPermission(player.getUniqueId(), permission, source);
    }

    public static void unsetPermission(UUID uuid, String permission, @Nullable CommandSource source) {
        permissions(uuid).remove(permission);
        if (source != null) {
            new PlayerPermissionChangeEvent(uuid, permission, PermissionChangeEvent.Type.UNSET, source).call();
        }
        TNLPlayer player = TNLPlayer.cast(uuid);
        if (player != null) update(player);
    }

    public static void exportAll() {
        JsonObject root = new JsonObject();
        permissions.forEach((uuid, permissions) -> {
            JsonObject individual = new JsonObject();
            JsonArray allowed = new JsonArray();
            JsonArray denied = new JsonArray();
            permissions.forEach((permission, allow) -> {
                if (allow) allowed.add(permission);
                else denied.add(permission);
            });
            individual.add("allowed", allowed);
            individual.add("denied", denied);
            root.add(uuid.toString(), individual);
        });
        FILE.setRoot(root).save();
    }

    public static void loadAll() {
        HashMap<UUID, HashMap<String, Boolean>> permissions = new HashMap<>();
        JsonObject root = FILE.getRoot().getAsJsonObject();
        for (OfflinePlayer all : Bukkit.getOfflinePlayers()) {
            String uuid = all.getUniqueId().toString();
            if (!root.has(uuid) || !root.get(uuid).isJsonObject()) continue;
            JsonObject object = root.getAsJsonObject(uuid);
            HashMap<String, Boolean> individual = new HashMap<>();
            if (object.has("allowed") && object.get("allowed").isJsonArray()) {
                object.getAsJsonArray("allowed").forEach(element -> individual.put(element.getAsString(), true));
            }
            if (object.has("denied") && object.get("denied").isJsonArray()) {
                object.getAsJsonArray("denied").forEach(element -> individual.put(element.getAsString(), false));
            }
            permissions.put(all.getUniqueId(), individual);
        }
        Permissions.permissions.putAll(permissions);
    }

    public static List<String> getAllPermissions(OfflinePlayer exclude) {
        List<String> permissions = getAllPermissions();
        permissions.removeAll(getAllowedPermissions(exclude));
        return permissions;
    }

    public static List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>();
        Permissions.permissions.values().forEach(map -> map.keySet().forEach(permission -> {
            if (!permissions.contains(permission)) permissions.add(permission);
        }));
        return permissions;
    }
}
