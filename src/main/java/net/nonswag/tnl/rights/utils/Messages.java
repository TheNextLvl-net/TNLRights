package net.nonswag.tnl.rights.utils;

import net.nonswag.tnl.core.api.file.formats.MessageFile;
import net.nonswag.tnl.core.api.message.Message;
import net.nonswag.tnl.core.api.message.key.MessageKey;

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
        MessageFile messages = Message.getEnglish();
        messages.setDefault(MEMBER_ADDED, "%prefix% §3§o%source%§b§o added §3§o%member%§b§o to group §3§o%group%");
        messages.setDefault(MEMBER_REMOVED, "%prefix% §3§o%source%§b§o removed §3§o%member%§b§o from group §3§o%group%");
        messages.save();
    }

    private static void initGerman() {
        MessageFile messages = Message.getGerman();
        messages.setDefault(MEMBER_ADDED, "%prefix% §3§o%member%§b§o wurde von §3§o%source%§b§o in die gruppe §3§o%group%§b§o hinzugefügt");
        messages.setDefault(MEMBER_REMOVED, "%prefix% §3§o%member%§b§o wurde von §3§o%source%§b§o aus der gruppe §3§o%group%§b§o entfernt");
        messages.save();
    }
}
