package org.alloytools.alloy.model.api;

import java.util.List;

import org.alloytools.alloy.builder.api.ValueBuilder;

public interface Environment {

    List<Typed> resolve(String name);

    TType INT();

    TValue resolve(ValueBuilder value);
}
