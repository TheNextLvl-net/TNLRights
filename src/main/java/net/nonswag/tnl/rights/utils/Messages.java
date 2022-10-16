package net.nonswag.tnl.rights.utils;

import net.nonswag.core.api.message.Message;
import net.nonswag.core.api.message.key.MessageKey;

import javax.annotation.Nonnull;

public class Messages {

    @Nonnull
    public static MessageKey MEMBER_ADDED = new MessageKey("group-member-added");
    @Nonnull
    public static MessageKey MEMBER_REMOVED = new MessageKey("group-member-removed");

    public static void init() {
        initEnglish();
        initGerman();
    }

    private static void initEnglish() {
        Message.getEnglish().setDefault(MEMBER_ADDED, "%prefix% §3§o%source%§b§o added §3§o%member%§b§o to group §3§o%group%");
        Message.getEnglish().setDefault(MEMBER_REMOVED, "%prefix% §3§o%source%§b§o removed §3§o%member%§b§o from group §3§o%group%");
        Message.getEnglish().save();
    }

    private static void initGerman() {
        Message.getGerman().setDefault(MEMBER_ADDED, "%prefix% §3§o%member%§b§o wurde von §3§o%source%§b§o in die gruppe §3§o%group%§b§o hinzugefügt");
        Message.getGerman().setDefault(MEMBER_REMOVED, "%prefix% §3§o%member%§b§o wurde von §3§o%source%§b§o aus der gruppe §3§o%group%§b§o entfernt");
        Message.getGerman().save();
    }
}
