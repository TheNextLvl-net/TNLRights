package net.nonswag.tnl.rights.api.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.nonswag.core.api.file.formats.PropertyFile;

@Getter
@Accessors(fluent = true)
@Setter(AccessLevel.PRIVATE)
public class Config {
    @Getter
    @Accessors(fluent = false)
    private static final Config instance = new Config();

    private final PropertyFile configuration = new PropertyFile("plugins/Rights", "config.properties");

    private boolean safetyFeature = true;
    private boolean onlyConsoleCanOp = true;

    private Config() {
        safetyFeature(configuration.getBoolean("safety-feature", safetyFeature()));
        onlyConsoleCanOp(configuration.getBoolean("only-console-can-op", onlyConsoleCanOp()));
        export();
    }

    public void export() {
        configuration.set("safety-feature", safetyFeature());
        configuration.set("only-console-can-op", onlyConsoleCanOp());
        configuration.save();
    }
}
