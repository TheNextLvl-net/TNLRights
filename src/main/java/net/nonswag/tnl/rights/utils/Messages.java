package net.nonswag.tnl.rights.utils;

import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.file.formats.MessageFile;
import net.nonswag.core.api.language.Language;
import net.nonswag.core.api.message.key.MessageKey;

@FieldsAreNonnullByDefault
public class Messages {
    public static MessageKey MEMBER_ADDED = new MessageKey("group-member-added").register();
    public static MessageKey MEMBER_REMOVED = new MessageKey("group-member-removed").register();

    public static void init() {
        initEnglish();
        initGerman();
    }

    private static void initEnglish() {
        MessageFile english = MessageFile.getOrCreate(Language.AMERICAN_ENGLISH);
        english.setDefault(MEMBER_ADDED, "%prefix% §3§o%source%§b§o added §3§o%member%§b§o to group §3§o%group%");
        english.setDefault(MEMBER_REMOVED, "%prefix% §3§o%source%§b§o removed §3§o%member%§b§o from group §3§o%group%");
        english.save();
    }

    private static void initGerman() {
        MessageFile german = MessageFile.getOrCreate(Language.GERMAN);
        german.setDefault(MEMBER_ADDED, "%prefix% §3§o%member%§b§o wurde von §3§o%source%§b§o in die gruppe §3§o%group%§b§o hinzugefügt");
        german.setDefault(MEMBER_REMOVED, "%prefix% §3§o%member%§b§o wurde von §3§o%source%§b§o aus der gruppe §3§o%group%§b§o entfernt");
        german.save();
    }
}
