package org.alloytools.alloy.builder.api;

import java.util.List;

import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TValue;

public interface ValueBuilder extends ExprBuilder {

    List<TValue> resolve(Environment env);
}
