package net.nonswag.tnl.rights.utils;

import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.rights.api.group.Group;

public final class Placeholders {

    public static void init() {
        Placeholder.Registry.register(new Placeholder("group_prefix", player -> Group.get((TNLPlayer) player).getTeam().getPrefix()));
        Placeholder.Registry.register(new Placeholder("group_suffix", player -> Group.get((TNLPlayer) player).getTeam().getSuffix()));
        Placeholder.Registry.register(new Placeholder("group_color", player -> Group.get((TNLPlayer) player).getTeam().getColor().name().toLowerCase().replace("_", " ")));
        Placeholder.Registry.register(new Placeholder("group", player -> Group.get((TNLPlayer) player).getName()));
    }
}
