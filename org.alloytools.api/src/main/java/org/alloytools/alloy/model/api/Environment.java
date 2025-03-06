package org.alloytools.alloy.model.api;

import java.util.List;

public interface Environment {

    List<Typed> resolve(String name);

    TType INT();
}
