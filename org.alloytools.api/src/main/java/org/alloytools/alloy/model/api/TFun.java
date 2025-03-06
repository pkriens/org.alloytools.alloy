package org.alloytools.alloy.model.api;

import java.util.List;

public interface TFun extends Typed {

    String name();

    TModule module();

    List<TDeclaration> arguments();

    Multiplicity multiplicity();

    TValue domain();

    TValue body();
}
