package net.nonswag.tnl.rights.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.nonswag.tnl.core.api.file.formats.JsonFile;
import net.nonswag.tnl.core.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.scoreboard.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class Group {

    @Nonnull
    private static final HashMap<String, Group> groups = new HashMap<>();
    @Nonnull
    public static final Group DEFAULT = new Group("default").register();

    @Nonnull
    private final String name;
    @Nonnull
    private final Map<String, Boolean> permissions = new HashMap<>();
    @Nonnull
    private final List<UUID> members = new ArrayList<>();
    @Nonnull
    private final JsonFile file;
    @Nonnull
    private final Team team;

    public Group(@Nonnull String name) {
        if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
        if (!name.matches("[\\w]*")) throw new IllegalArgumentException("'" + name.replaceAll("[\\w]*", "") + "'");
        this.name = name;
        this.file = new JsonFile("plugins/Rights/Groups", getName() + ".json");
        this.team = name.equals("default") ? Team.NONE : new Team(loadId());
        load();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    private JsonFile getFile() {
        return file;
    }

    @Nonnull
    public Team getTeam() {
        return team;
    }

    private int loadId() {
        JsonObject root = file.getJsonElement().getAsJsonObject();
        if (!root.has("id")) return Team.NONE.getId() - getGroups().size() + 1;
        return root.get("id").getAsInt();
    }

    protected void load() {
        JsonObject root = file.getJsonElement().getAsJsonObject();
        if (!root.has("prefix")) root.addProperty("prefix", getTeam().getPrefix());
        if (!root.has("suffix")) root.addProperty("suffix", getTeam().getSuffix());
        if (!root.has("color")) root.addProperty("color", getTeam().getColor().name());
        getTeam().setPrefix(root.get("prefix").getAsString());
        getTeam().setSuffix(root.get("suffix").getAsString());
        String color = root.get("color").getAsString().toUpperCase();
        this.permissions.clear();
        this.members.clear();
        try {
            getTeam().setColor(ChatColor.valueOf(color));
        } catch (IllegalArgumentException e) {
            Logger.error.println("Invalid color <'" + color + "'>");
        }
        if (!root.has("permissions") || !root.get("permissions").isJsonObject()) {
            root.add("permissions", new JsonObject());
        }
        JsonObject perms = root.getAsJsonObject("permissions");
        if (!perms.has("allowed") || !perms.get("allowed").isJsonObject()) perms.add("allowed", new JsonArray());
        if (!perms.has("denied") || !perms.get("denied").isJsonObject()) perms.add("denied", new JsonArray());
        for (JsonElement permission : perms.getAsJsonArray("allowed")) {
            this.permissions.put(permission.getAsString(), true);
        }
        for (JsonElement permission : perms.getAsJsonArray("denied")) {
            this.permissions.put(permission.getAsString(), false);
        }
        if (!root.has("members") || !root.get("members").isJsonArray()) root.add("members", new JsonArray());
        for (JsonElement member : root.getAsJsonArray("members")) {
            try {
                OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(member.getAsString());
                if (player != null) addPlayer(player);
                else addPlayer(UUID.fromString(member.getAsString()));
            } catch (Exception ignored) {
            }
        }
        export();
    }

    public void export() {
        JsonObject root = file.getJsonElement().getAsJsonObject();
        root.addProperty("prefix", getTeam().getPrefix());
        root.addProperty("suffix", getTeam().getSuffix());
        root.addProperty("color", getTeam().getColor().name());
        root.addProperty("id", getTeam().getId());
        JsonObject permissions = new JsonObject();
        JsonArray allowed = new JsonArray();
        JsonArray denied = new JsonArray();
        this.permissions.forEach((s, b) -> {
            if (b) allowed.add(s);
            else denied.add(s);
        });
        permissions.add("allowed", allowed);
        permissions.add("denied", denied);
        root.add("permissions", permissions);
        JsonArray members = new JsonArray();
        for (UUID member : this.members) members.add(member.toString());
        root.add("members", members);
        file.save();
    }

    public boolean delete() {
        if (equals(DEFAULT)) return false;
        file.delete();
        if (isRegistered()) unregister();
        return true;
    }

    @Nonnull
    public Group register() {
        if (isRegistered()) throw new IllegalArgumentException("Group already registered");
        groups.put(getName(), this);
        return this;
    }

    @Nonnull
    public Group unregister() {
        if (!isRegistered()) throw new IllegalArgumentException("Group not registered");
        groups.remove(getName());
        return this;
    }

    @Nonnull
    public Map<String, Boolean> getPermissions() {
        return ImmutableMap.copyOf(permissions);
    }

    @Nonnull
    public List<UUID> getMembers() {
        return ImmutableList.copyOf(members);
    }

    @Nonnull
    public List<String> getAllowedPermissions() {
        List<String> permissions = new ArrayList<>();
        this.permissions.forEach((s, b) -> {
            if (b) permissions.add(s);
        });
        return permissions;
    }

    @Nonnull
    public List<String> getDeniedPermissions() {
        List<String> permissions = new ArrayList<>();
        this.permissions.forEach((s, b) -> {
            if (!b) permissions.add(s);
        });
        return permissions;
    }

    public boolean hasPermission(@Nonnull String permission) {
        return permissions.getOrDefault(permission, false);
    }

    public boolean isPermissionSet(@Nonnull String permission) {
        return permissions.containsKey(permission);
    }

    public void addPermission(@Nonnull String permission) {
        permissions.put(permission, true);
        updatePermissions();
    }

    public void removePermission(@Nonnull String permission) {
        permissions.put(permission, false);
        updatePermissions();
    }

    public void unsetPermission(@Nonnull String permission) {
        permissions.remove(permission);
        updatePermissions();
    }

    public void addPlayer(@Nonnull OfflinePlayer player) {
        addPlayer(player.getUniqueId());
    }

    public void addPlayer(@Nonnull UUID player) {
        get(player).removePlayer(player);
        members.add(player);
        updatePermissions(player);
        updateMember(player);
    }

    public void removePlayer(@Nonnull OfflinePlayer player) {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(@Nonnull UUID player) {
        members.remove(player);
        if (!equals(DEFAULT)) DEFAULT.addPlayer(player);
    }

    public boolean isMember(@Nonnull OfflinePlayer player) {
        return isMember(player.getUniqueId());
    }

    public boolean isMember(@Nonnull UUID player) {
        if (equals(DEFAULT) && !hasGroup(player)) return true;
        return members.contains(player);
    }

    public boolean isRegistered() {
        return groups.containsKey(getName());
    }

    public void updatePermissions() {
        for (UUID member : members) updatePermissions(member);
    }

    public void updatePermissions(@Nonnull OfflinePlayer member) {
        updatePermissions(member.getUniqueId());
    }

    public void updatePermissions(@Nonnull UUID member) {
        if (!isMember(member)) return;
        TNLPlayer player = TNLPlayer.cast(member);
        if (player == null) return;
        for (String permission : getAllowedPermissions()) {
            if (!player.permissionManager().isPermissionSet(permission) || !Permissions.isPermissionSet(member, permission)) {
                player.permissionManager().addPermission(permission);
            }
        }
        for (String permission : getDeniedPermissions()) {
            if (!player.permissionManager().isPermissionSet(permission) || !Permissions.isPermissionSet(member, permission)) {
                player.permissionManager().removePermission(permission);
            }
        }
    }

    public void updateMembers() {
        for (UUID member : members) updateMember(member);
    }

    public void updatePlayers() {
        for (Player all : Bukkit.getOnlinePlayers()) updateMember(all);
    }

    public void updateMember(@Nonnull OfflinePlayer member) {
        updateMember(member.getUniqueId());
    }

    public void updateMember(@Nonnull UUID member) {
        if (!isMember(member)) return;
        TNLPlayer player = TNLPlayer.cast(member);
        if (player != null) player.scoreboardManager().setTeam(getTeam());
    }

    @Nonnull
    public static List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>();
        for (Group group : getGroups()) permissions.addAll(group.getPermissions().keySet());
        return permissions;
    }

    @Nonnull
    public static List<Group> getGroups() {
        return ImmutableList.copyOf(groups.values());
    }

    @Nullable
    public static Group get(@Nonnull String name) {
        return groups.get(name);
    }

    @Nonnull
    public static Group get(@Nonnull OfflinePlayer player) {
        return get(player.getUniqueId());
    }

    @Nonnull
    public static Group get(@Nonnull UUID player) {
        for (Group group : Group.getGroups()) if (group.members.contains(player)) return group;
        return DEFAULT;
    }

    public static boolean hasGroup(@Nonnull OfflinePlayer player) {
        return hasGroup(player.getUniqueId());
    }

    public static boolean hasGroup(@Nonnull UUID player) {
        for (Group group : Group.getGroups()) if (group.members.contains(player)) return true;
        return false;
    }

    public static void exportAll() {
        for (Group group : getGroups()) group.export();
    }

    public static void loadAll() {
        File groups = new File("plugins/Rights/Groups");
        File[] files = groups.listFiles((file, name) -> name.length() > 5 && !name.equals("default.json") && name.matches("[\\w]*.json"));
        if (files == null) return;
        for (File file : files) new Group(file.getName().substring(0, file.getName().length() - 5)).register();
    }
}
