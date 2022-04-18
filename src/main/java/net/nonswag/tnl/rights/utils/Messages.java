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
        messages.setDefault(MEMBER_ADDED, "%prefix% §6%source%§a added §6%member%§a to group §6%group%");
        messages.setDefault(MEMBER_REMOVED, "%prefix% §6%source%§a removed §6%member%§a from group §6%group%");
        messages.save();
    }

    private static void initGerman() {
        MessageFile messages = Message.getGerman();
        messages.setDefault(MEMBER_ADDED, "%prefix% §6%member%§a wurde von §6%source% in die gruppe §6%group%§a hinzugefügt");
        messages.setDefault(MEMBER_REMOVED, "%prefix% §6%member%§a wurde von §6%source% aus der gruppe §6%group%§a entfernt");
        messages.save();
    }
}
