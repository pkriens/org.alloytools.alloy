package org.alloytools.alloy.model.api;

import org.alloytools.alloy.builder.api.Pos;

/**
 * A warning or error from the compilation
 */
public record UserMessage(Severity severity, Pos< ? > where, String message) {

    public enum Severity {
                          DEBUG,
                          INFO,
                          WARNING,
                          ERROR
    };

}
