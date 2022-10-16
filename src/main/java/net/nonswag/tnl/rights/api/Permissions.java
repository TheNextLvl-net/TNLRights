package net.nonswag.tnl.rights.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.file.formats.JsonFile;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;
import net.nonswag.tnl.rights.events.player.PlayerPermissionChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Permissions {

    @Nonnull
    private static final Map<UUID, Map<String, Boolean>> permissions = new HashMap<>();
    @Nonnull
    private static final JsonFile FILE = new JsonFile("plugins/Rights", "permissions.json");

    @Nonnull
    public static Map<String, Boolean> getPermissions(@Nonnull OfflinePlayer player) {
        return getPermissions(player.getUniqueId());
    }

    @Nonnull
    public static Map<String, Boolean> getPermissions(@Nonnull UUID uuid) {
        if (!getPermissions().containsKey(uuid)) getPermissions().put(uuid, new HashMap<>());
        return getPermissions().get(uuid);
    }

    @Nonnull
    public static List<String> getAllowedPermissions(@Nonnull OfflinePlayer player) {
        return getAllowedPermissions(player.getUniqueId());
    }

    @Nonnull
    public static List<String> getAllowedPermissions(@Nonnull UUID uuid) {
        List<String> permissions = new ArrayList<>();
        getPermissions(uuid).forEach((permission, allowed) -> {
            if (allowed) permissions.add(permission);
        });
        return permissions;
    }

    @Nonnull
    public static List<String> getDeniedPermissions(@Nonnull OfflinePlayer player) {
        return getDeniedPermissions(player.getUniqueId());
    }

    @Nonnull
    public static List<String> getDeniedPermissions(@Nonnull UUID uuid) {
        List<String> permissions = new ArrayList<>();
        getPermissions(uuid).forEach((permission, allowed) -> {
            if (!allowed) permissions.add(permission);
        });
        return permissions;
    }

    public static void setPermissions(@Nonnull OfflinePlayer player, @Nonnull Map<String, Boolean> permissions) {
        setPermissions(player.getUniqueId(), permissions);
    }

    public static void setPermissions(@Nonnull UUID uuid, @Nonnull Map<String, Boolean> permissions) {
        getPermissions().put(uuid, permissions);
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.permissionManager().setPermissions(getPermissions(online.bukkit()));
    }

    public static boolean hasPermissions(@Nonnull OfflinePlayer player) {
        return hasPermissions(player.getUniqueId());
    }

    public static boolean hasPermissions(@Nonnull UUID uuid) {
        return getPermissions().containsKey(uuid) && !getPermissions(uuid).isEmpty();
    }

    public static boolean hasPermission(@Nonnull OfflinePlayer player, @Nonnull String permission) {
        return hasPermission(player.getUniqueId(), permission);
    }

    public static boolean hasPermission(@Nonnull UUID uuid, @Nonnull String permission) {
        return getPermissions(uuid).getOrDefault(permission, false);
    }

    public static boolean isPermissionSet(@Nonnull OfflinePlayer player, @Nonnull String permission) {
        return isPermissionSet(player.getUniqueId(), permission);
    }

    public static boolean isPermissionSet(@Nonnull UUID uuid, @Nonnull String permission) {
        return getPermissions(uuid).containsKey(permission);
    }

    public static void addPermission(@Nonnull OfflinePlayer player, @Nonnull String permission, @Nullable CommandSource source) {
        addPermission(player.getUniqueId(), permission, source);
    }

    public static void addPermission(@Nonnull UUID uuid, @Nonnull String permission, @Nullable CommandSource source) {
        getPermissions(uuid).put(permission, true);
        if (source != null) {
            new PlayerPermissionChangeEvent(uuid, permission, PermissionChangeEvent.Type.GRANT, source).call();
        }
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.permissionManager().setPermissions(getPermissions(online.bukkit()));
    }

    public static void removePermission(@Nonnull OfflinePlayer player, @Nonnull String permission, @Nullable CommandSource source) {
        removePermission(player.getUniqueId(), permission, source);
    }

    public static void removePermission(@Nonnull UUID uuid, @Nonnull String permission, @Nullable CommandSource source) {
        getPermissions(uuid).put(permission, false);
        if (source != null) {
            new PlayerPermissionChangeEvent(uuid, permission, PermissionChangeEvent.Type.REVOKE, source).call();
        }
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.permissionManager().setPermissions(getPermissions(online.bukkit()));
    }

    public static void unsetPermission(@Nonnull OfflinePlayer player, @Nonnull String permission, @Nullable CommandSource source) {
        unsetPermission(player.getUniqueId(), permission, source);
    }

    public static void unsetPermission(@Nonnull UUID uuid, @Nonnull String permission, @Nullable CommandSource source) {
        getPermissions(uuid).remove(permission);
        if (source != null) {
            new PlayerPermissionChangeEvent(uuid, permission, PermissionChangeEvent.Type.UNSET, source).call();
        }
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.permissionManager().setPermissions(getPermissions(online.bukkit()));
    }

    @Nonnull
    public static Map<UUID, Map<String, Boolean>> getPermissions() {
        return permissions;
    }

    public static void exportAll() {
        JsonObject root = new JsonObject();
        getPermissions().forEach((uuid, permissions) -> {
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
        FILE.setJsonElement(root);
        FILE.save();
    }

    public static void loadAll() {
        HashMap<UUID, HashMap<String, Boolean>> permissions = new HashMap<>();
        JsonObject root = FILE.getJsonElement().getAsJsonObject();
        for (OfflinePlayer all : Bukkit.getOfflinePlayers()) {
            String uuid = all.getUniqueId().toString();
            if (root.has(uuid) && root.get(uuid).isJsonObject()) {
                JsonObject object = root.getAsJsonObject(uuid);
                HashMap<String, Boolean> individual = new HashMap<>();
                if (object.has("allowed") && object.get("allowed").isJsonArray()) {
                    for (JsonElement element : object.getAsJsonArray("allowed")) {
                        individual.put(element.getAsString(), true);
                    }
                }
                if (object.has("denied") && object.get("denied").isJsonArray()) {
                    for (JsonElement element : object.getAsJsonArray("denied")) {
                        individual.put(element.getAsString(), false);
                    }
                }
                permissions.put(all.getUniqueId(), individual);
            }
        }
        getPermissions().putAll(permissions);
    }

    @Nonnull
    public static List<String> getAllPermissions(@Nonnull OfflinePlayer exclude) {
        List<String> permissions = getAllPermissions();
        permissions.removeAll(getAllowedPermissions(exclude));
        return permissions;
    }

    @Nonnull
    public static List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>();
        for (Map<String, Boolean> map : getPermissions().values()) {
            for (String permission : map.keySet()) {
                if (permissions.contains(permission)) continue;
                permissions.add(permission);
            }
        }
        return permissions;
    }
}
