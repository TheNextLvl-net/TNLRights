package net.nonswag.tnl.rights.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.nonswag.tnl.core.api.file.formats.JsonFile;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.*;

public class Permissions extends JsonFile {

    @Nonnull
    private static final Permissions instance = new Permissions();

    @Nonnull
    private final Map<UUID, Map<String, Boolean>> permissions = new HashMap<>();

    private Permissions() {
        super("plugins/Rights", "permissions.json");
    }

    @Nonnull
    public Map<String, Boolean> getPermissions(@Nonnull OfflinePlayer player) {
        return getPermissions(player.getUniqueId());
    }

    @Nonnull
    public Map<String, Boolean> getPermissions(@Nonnull UUID uuid) {
        if (!getPermissions().containsKey(uuid)) getPermissions().put(uuid, new HashMap<>());
        return getPermissions().get(uuid);
    }

    @Nonnull
    public List<String> getAllowedPermissions(@Nonnull OfflinePlayer player) {
        return getAllowedPermissions(player.getUniqueId());
    }

    @Nonnull
    public List<String> getAllowedPermissions(@Nonnull UUID uuid) {
        List<String> permissions = new ArrayList<>();
        getPermissions(uuid).forEach((permission, allowed) -> {
            if (allowed) permissions.add(permission);
        });
        return permissions;
    }

    @Nonnull
    public List<String> getDeniedPermissions(@Nonnull OfflinePlayer player) {
        return getDeniedPermissions(player.getUniqueId());
    }

    @Nonnull
    public List<String> getDeniedPermissions(@Nonnull UUID uuid) {
        List<String> permissions = new ArrayList<>();
        getPermissions(uuid).forEach((permission, allowed) -> {
            if (!allowed) permissions.add(permission);
        });
        return permissions;
    }

    public void setPermissions(@Nonnull OfflinePlayer player, @Nonnull Map<String, Boolean> permissions) {
        setPermissions(player.getUniqueId(), permissions);
    }

    public void setPermissions(@Nonnull UUID uuid, @Nonnull Map<String, Boolean> permissions) {
        getPermissions().put(uuid, permissions);
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.getPermissionManager().setPermissions(getPermissions(online));
    }

    public boolean hasPermission(@Nonnull OfflinePlayer player, @Nonnull String permission) {
        return hasPermission(player.getUniqueId(), permission);
    }

    public boolean hasPermission(@Nonnull UUID uuid, @Nonnull String permission) {
        return getPermissions(uuid).getOrDefault(permission, false);
    }

    public boolean containsPermission(@Nonnull OfflinePlayer player, @Nonnull String permission) {
        return containsPermission(player.getUniqueId(), permission);
    }

    public boolean containsPermission(@Nonnull UUID uuid, @Nonnull String permission) {
        return getPermissions(uuid).containsKey(permission);
    }

    public void addPermission(@Nonnull OfflinePlayer player, @Nonnull String permission) {
        addPermission(player.getUniqueId(), permission);
    }

    public void addPermission(@Nonnull UUID uuid, @Nonnull String permission) {
        getPermissions(uuid).put(permission, true);
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.getPermissionManager().setPermissions(getPermissions(online));
    }

    public void removePermission(@Nonnull OfflinePlayer player, @Nonnull String permission) {
        removePermission(player.getUniqueId(), permission);
    }

    public void removePermission(@Nonnull UUID uuid, @Nonnull String permission) {
        getPermissions(uuid).put(permission, false);
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.getPermissionManager().setPermissions(getPermissions(online));
    }

    public void unsetPermission(@Nonnull OfflinePlayer player, @Nonnull String permission) {
        unsetPermission(player.getUniqueId(), permission);
    }

    public void unsetPermission(@Nonnull UUID uuid, @Nonnull String permission) {
        getPermissions(uuid).remove(permission);
        TNLPlayer online = TNLPlayer.cast(uuid);
        if (online != null) online.getPermissionManager().setPermissions(getPermissions(online));
    }

    @Nonnull
    public Map<UUID, Map<String, Boolean>> getPermissions() {
        return permissions;
    }

    public void exportAll() {
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
        setJsonElement(root);
        save();
    }

    public void loadAll() {
        HashMap<UUID, HashMap<String, Boolean>> permissions = new HashMap<>();
        JsonObject root = getJsonElement().getAsJsonObject();
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
    public static Permissions getInstance() {
        return instance;
    }
}
