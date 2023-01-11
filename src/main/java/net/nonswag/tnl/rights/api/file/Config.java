package net.nonswag.tnl.rights.api.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.nonswag.core.api.file.formats.PropertiesFile;

@Getter
@Accessors(fluent = true)
@Setter(AccessLevel.PRIVATE)
public class Config {
    @Getter
    @Accessors(fluent = false)
    private static final Config instance = new Config();

    private final PropertiesFile configuration = new PropertiesFile("plugins/Rights", "config.properties");

    private boolean safetyFeature = true;
    private boolean onlyConsoleCanOp = true;

    private Config() {
        safetyFeature(configuration.getRoot().getBoolean("safety-feature", safetyFeature()));
        onlyConsoleCanOp(configuration.getRoot().getBoolean("only-console-can-op", onlyConsoleCanOp()));
        export();
    }

    public void export() {
        configuration.getRoot().set("safety-feature", safetyFeature());
        configuration.getRoot().set("only-console-can-op", onlyConsoleCanOp());
        configuration.save();
    }
}
