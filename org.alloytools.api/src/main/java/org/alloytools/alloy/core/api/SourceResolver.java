package org.alloytools.alloy.core.api;

import java.net.URI;
import java.util.Collection;

import org.alloytools.alloy.model.api.UserMessage;

/**
 * A content resolver for translating names to content. This is for example
 * useful if some files are in a window instead of on disk. Can also provide
 * caching if so desired.
 */
public interface SourceResolver {

    interface SourceManager extends SourceResolver {

        void module(String moduleName);

        void set(String source);

        String source();

        URI path();

        void messages(Collection<UserMessage> messages);
    }

    /**
     * Resolve a path to a source string
     *
     * @param path the path referenced in an Alloy file
     * @return the content associated with the given path
     */
    SourceManager resolve(String path);
}
