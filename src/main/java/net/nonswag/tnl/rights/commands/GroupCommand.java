package net.nonswag.tnl.rights.commands;

import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GroupCommand extends TNLCommand {

    public GroupCommand() {
        super("group", "tnl.rights");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        /*
        group list (group)
        group create [name]
        group delete [group]

        group players add [group] [player]
        group players remove [group] [player]

        group rights list [group]
        group rights disallow [group] [permission]
        group rights remove [group] [permission]
        group rights add [group] [permission]
         */
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            suggestions.add("list");
            suggestions.add("create");
            suggestions.add("delete");
            suggestions.add("add");
            suggestions.add("players");
            suggestions.add("rights");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("list")) {
                // add groups
            } else if (args[0].equalsIgnoreCase("players")) {
                suggestions.add("add");
                suggestions.add("remove");
            } else if (args[0].equalsIgnoreCase("rights")) {
                suggestions.add("list");
                suggestions.add("disallow");
                suggestions.add("remove");
                suggestions.add("add");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("players")) {
                if (args[1].equalsIgnoreCase("add")) {
                    // add if not in
                } else if (args[1].equalsIgnoreCase("remove")) {
                    // remove if in
                }
                suggestions.add("remove");
            } else if (args[0].equalsIgnoreCase("rights")) {
                if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("disallow") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("add")) {
                    // add groups
                }
            }
        }
        return suggestions;
    }
}
