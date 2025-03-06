package org.alloytools.alloy.core.api;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

import org.alloytools.alloy.model.api.TModule;

public interface AlloyCompiler {


    TModule compile(URI path);

    TModule compile(String source, URI path);

    TModule compile(TModule parent, String subModule);

    default TModule compile(File file) {
        return compile(file.toURI());
    }

    default TModule compile(Path file) {
        return compile(file.toFile().toURI());
    }


}
