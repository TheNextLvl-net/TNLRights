package net.nonswag.tnl.rights.api.errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GroupException extends IllegalArgumentException {

    protected GroupException(String message) {
        super(message);
    }

    protected GroupException(String message, Throwable cause) {
        super(message, cause);
    }

    protected GroupException(Throwable cause) {
        super(cause);
    }
}
