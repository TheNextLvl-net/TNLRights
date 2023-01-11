package net.nonswag.tnl.rights.api.group;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.annotation.MethodsReturnNonnullByDefault;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.file.formats.JsonFile;
import net.nonswag.core.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.scoreboard.Team;
import net.nonswag.tnl.rights.api.errors.GroupException;
import net.nonswag.tnl.rights.api.errors.InvalidGroupNameException;
import net.nonswag.tnl.rights.api.permissions.Permissions;
import net.nonswag.tnl.rights.events.PermissionChangeEvent;
import net.nonswag.tnl.rights.events.group.GroupMemberAddEvent;
import net.nonswag.tnl.rights.events.group.GroupMemberRemoveEvent;
import net.nonswag.tnl.rights.events.group.GroupPermissionChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.*;

@ToString
@EqualsAndHashCode
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Group {

    private static final HashMap<String, Group> groups = new HashMap<>();
    public static final Group DEFAULT = new Group("default") {
        @Override
        public List<UUID> getMembers() {
            List<UUID> members = new ArrayList<>();
            for (OfflinePlayer all : Bukkit.getOfflinePlayers()) if (isMember(all)) members.add(all.getUniqueId());
            return members;
        }

        @Override
        public boolean isMember(UUID player) {
            return !hasGroup(player);
        }

        @Override
        public boolean delete() {
            return false;
        }

        @Override
        public void removePlayer(UUID player, @Nullable CommandSource source) {
        }
    }.register();

    @Getter
    private final String name;
    @Getter
    private final Team team;
    private final Map<String, Boolean> permissions = new HashMap<>();
    private final List<UUID> members = new ArrayList<>();
    private final JsonFile file;
    @Getter
    @Nullable
    private net.nonswag.core.api.object.Getter<Group> child, parent;

    public Group(String name) throws GroupException {
        if (name.isEmpty()) throw new InvalidGroupNameException("name cannot be empty");
        if (!name.matches("\\w*")) throw new InvalidGroupNameException("'" + name.replaceAll("\\w*", "") + "'");
        this.name = name;
        this.file = new JsonFile("plugins/Rights/Groups", getName() + ".json");
        this.team = name.equals("default") ? Team.NONE : new Team(loadId());
        load();
    }

    public boolean setChild(@Nullable Group child) {
        if (equals(child) || Objects.equals(child, this.child != null ? this.child.get() : null)) return false;
        return (this.child = child != null ? () -> child : null) == null || child.setParent(this);
    }

    public boolean setParent(@Nullable Group parent) {
        if (equals(parent) || Objects.equals(parent, this.parent != null ? this.parent.get() : null)) return false;
        return (this.parent = parent != null ? () -> parent : null) == null || parent.setChild(this);
    }

    private int loadId() {
        if (isDefault()) return Integer.MAX_VALUE;
        JsonObject root = file.getJsonElement().getAsJsonObject();
        if (!root.has("id")) return Team.NONE.getId() - (getGroups().size() + 1);
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
        if (!perms.has("allowed") || !perms.get("allowed").isJsonArray()) perms.add("allowed", new JsonArray());
        if (!perms.has("denied") || !perms.get("denied").isJsonArray()) perms.add("denied", new JsonArray());
        perms.getAsJsonArray("allowed").forEach(permission -> this.permissions.put(permission.getAsString(), true));
        perms.getAsJsonArray("denied").forEach(permission -> this.permissions.put(permission.getAsString(), false));
        if (!isDefault()) {
            if (!root.has("members") || !root.get("members").isJsonArray()) root.add("members", new JsonArray());
            root.getAsJsonArray("members").forEach(member -> addPlayer(UUID.fromString(member.getAsString()), null));
        }
        export();
    }

    public void export() {
        JsonObject root = new JsonObject();
        root.addProperty("prefix", getTeam().getPrefix());
        root.addProperty("suffix", getTeam().getSuffix());
        root.addProperty("color", getTeam().getColor().name());
        if (getParent() != null) root.addProperty("parent", getParent().get().getName());
        if (getChild() != null) root.addProperty("child", getChild().get().getName());
        if (!isDefault()) root.addProperty("id", getTeam().getId());
        JsonObject permissions = new JsonObject();
        JsonArray allowedPermissions = new JsonArray();
        JsonArray deniedPermissions = new JsonArray();
        this.permissions.forEach((permission, allowed) -> {
            if (allowed) allowedPermissions.add(permission);
            else deniedPermissions.add(permission);
        });
        permissions.add("allowed", allowedPermissions);
        permissions.add("denied", deniedPermissions);
        root.add("permissions", permissions);
        if (!isDefault()) {
            JsonArray members = new JsonArray();
            this.members.forEach(member -> members.add(member.toString()));
            root.add("members", members);
        }
        file.setJsonElement(root);
        file.save();
    }

    public boolean delete() {
        file.delete();
        if (isRegistered()) unregister();
        return true;
    }

    public Group register() {
        if (isRegistered()) throw new IllegalArgumentException("Group already registered");
        groups.put(getName(), this);
        return this;
    }

    public Group unregister() {
        if (!isRegistered()) throw new IllegalArgumentException("Group not registered");
        groups.remove(getName());
        return this;
    }

    public Map<String, Boolean> getPermissions() {
        return ImmutableMap.copyOf(permissions);
    }

    public List<UUID> getMembers() {
        return ImmutableList.copyOf(members);
    }

    public List<String> getAllowedPermissions() {
        List<String> permissions = new ArrayList<>();
        this.permissions.forEach((permission, allowed) -> {
            if (allowed) permissions.add(permission);
        });
        return permissions;
    }

    public List<String> getDeniedPermissions() {
        List<String> permissions = new ArrayList<>();
        this.permissions.forEach((permission, allowed) -> {
            if (!allowed) permissions.add(permission);
        });
        return permissions;
    }

    public boolean hasPermissions() {
        return !permissions.isEmpty();
    }

    public boolean hasPermission(String permission) {
        return permissions.getOrDefault(permission, false);
    }

    public boolean isPermissionSet(String permission) {
        return permissions.containsKey(permission);
    }

    private void updatePermissions() {
        getMembers().forEach(Permissions::update);
    }

    public void addPermission(String permission, @Nullable CommandSource source) {
        permissions.put(permission, true);
        updatePermissions();
        if (source == null) return;
        new GroupPermissionChangeEvent(this, permission, PermissionChangeEvent.Type.GRANT, source).call();
    }

    public void removePermission(String permission, @Nullable CommandSource source) {
        permissions.put(permission, false);
        updatePermissions();
        if (source == null) return;
        new GroupPermissionChangeEvent(this, permission, PermissionChangeEvent.Type.REVOKE, source).call();
    }

    public void unsetPermission(String permission, @Nullable CommandSource source) {
        permissions.remove(permission);
        updatePermissions();
        if (source == null) return;
        new GroupPermissionChangeEvent(this, permission, PermissionChangeEvent.Type.UNSET, source).call();
    }

    public void addPlayer(OfflinePlayer player, @Nullable CommandSource source) {
        addPlayer(player.getUniqueId(), source);
    }

    public void addPlayer(UUID player, @Nullable CommandSource source) {
        get(player).removePlayer(player, source);
        if (!isDefault()) members.add(player);
        Permissions.update(player);
        updateMembers();
        if (source != null) new GroupMemberAddEvent(this, player, source).call();
        updateMember(player);
    }

    public void removePlayer(OfflinePlayer player, @Nullable CommandSource source) {
        removePlayer(player.getUniqueId(), source);
    }

    public void removePlayer(UUID player, @Nullable CommandSource source) {
        members.remove(player);
        Permissions.update(player);
        if (source != null) new GroupMemberRemoveEvent(this, player, source).call();
        get(player).updateMember(player);
    }

    public boolean isMember(OfflinePlayer player) {
        return isMember(player.getUniqueId());
    }

    public boolean isMember(UUID player) {
        return members.contains(player);
    }

    public boolean isRegistered() {
        return groups.containsKey(getName());
    }

    public void updateMembers() {
        getMembers().forEach(this::updateMember);
    }

    public void updatePlayers() {
        Bukkit.getOnlinePlayers().forEach(this::updateMember);
    }

    public void updateMember(OfflinePlayer member) {
        updateMember(member.getUniqueId());
    }

    public void updateMember(TNLPlayer member) {
        updateMember(member.getUniqueId());
    }

    public void updateMember(UUID member) {
        if (!isMember(member)) return;
        TNLPlayer player = TNLPlayer.cast(member);
        if (player != null) player.scoreboardManager().setTeam(getTeam());
    }

    public final boolean isDefault() {
        return equals(DEFAULT);
    }

    public static List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>();
        getGroups().forEach(group -> permissions.addAll(group.getPermissions().keySet()));
        return permissions;
    }

    public static List<Group> getGroups() {
        return ImmutableList.copyOf(groups.values());
    }

    @Nullable
    public static Group get(String name) {
        return groups.get(name);
    }

    public static Group get(TNLPlayer player) {
        return get(player.getUniqueId());
    }

    public static Group get(OfflinePlayer player) {
        return get(player.getUniqueId());
    }

    public static Group get(UUID player) {
        for (Group group : Group.getGroups()) if (group.members.contains(player)) return group;
        return DEFAULT;
    }

    public static boolean hasGroup(OfflinePlayer player) {
        return hasGroup(player.getUniqueId());
    }

    public static boolean hasGroup(UUID player) {
        for (Group group : Group.getGroups()) if (group.members.contains(player)) return true;
        return false;
    }

    public static void exportAll() {
        getGroups().forEach(Group::export);
    }

    public static void loadAll() {
        File groups = new File("plugins/Rights/Groups");
        File[] files = groups.listFiles((file, name) -> name.length() > 5 && !name.equals("default.json") && name.matches("\\w*.json"));
        if (files == null) return;
        for (File file : files) new Group(file.getName().substring(0, file.getName().length() - 5)).register();
    }
}
