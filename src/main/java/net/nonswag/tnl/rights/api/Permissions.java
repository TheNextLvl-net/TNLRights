package net.nonswag.tnl.rights.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.nonswag.tnl.core.api.file.formats.JsonFile;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.*;

public class Permissions extends JsonFile {

    @Nonnull
    private static final Permissions instance = new Permissions();

    @Nonnull
    private final HashMap<UUID, List<String>> permissions = new HashMap<>();

    private Permissions() {
        super("plugins/Rights", "permissions.csv");
    }

    @Nonnull
    public List<String> getPermissions(@Nonnull TNLPlayer player) {
        return getPermissions(player.getUniqueId());
    }

    @Nonnull
    public List<String> getPermissions(@Nonnull UUID uuid) {
        if (!getPermissions().containsKey(uuid)) getPermissions().put(uuid, new ArrayList<>());
        return getPermissions().get(uuid);
    }

    public void setPermissions(@Nonnull TNLPlayer player, @Nonnull List<String> permissions) {
        setPermissions(player.getUniqueId(), permissions);
    }

    public void setPermissions(@Nonnull UUID uuid, @Nonnull List<String> permissions) {
        getPermissions().put(uuid, permissions);
    }

    @Nonnull
    public HashMap<UUID, List<String>> getPermissions() {
        return permissions;
    }

    public void exportAll() {
        JsonObject root = new JsonObject();
        for (UUID uuid : getPermissions().keySet()) {
            JsonArray array = new JsonArray();
            for (String permission : getPermissions(uuid)) array.add(permission);
            root.add(uuid.toString(), array);
        }
        setJsonElement(root);
        save();
    }

    public void loadAll() {
        HashMap<UUID, List<String>> permissions = new HashMap<>();
        JsonObject root = getJsonElement().getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            try {
                UUID uuid = UUID.fromString(entry.getKey());
                if (entry.getValue().isJsonArray()) {
                    List<String> rights = new ArrayList<>();
                    for (JsonElement element : entry.getValue().getAsJsonArray()) rights.add(element.getAsString());
                    permissions.put(uuid, rights);
                }
            } catch (Exception ignored) {
            }
        }
        getPermissions().putAll(permissions);
    }

    @Nonnull
    public static Permissions getInstance() {
        return instance;
    }
}
