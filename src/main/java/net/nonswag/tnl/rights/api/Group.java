package net.nonswag.tnl.rights.api;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class Group {

    @Nonnull
    private static final HashMap<String, Group> groups = new HashMap<>();

    @Nonnull
    private final String name;
    // permissions
    // players

    public Group(@Nonnull String name) {
        if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
        if (!name.matches("[\\w]*")) throw new IllegalArgumentException("'" + name.replaceAll("[\\w]*", "") + "'");
        this.name = name;
    }

    @Nonnull
    public String getName() {
        return name;
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

    public boolean isRegistered() {
        return groups.containsKey(getName());
    }

    @Nonnull
    public static List<Group> getGroups() {
        return ImmutableList.copyOf(groups.values());
    }

    @Nullable
    public static Group get(@Nonnull String name) {
        return groups.get(name);
    }
}
