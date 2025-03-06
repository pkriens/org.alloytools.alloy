package org.alloytools.alloy.builder.api;

import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TValue;

public interface ValueBuilder extends ExprBuilder {

    TValue resolve(Environment env);

}
