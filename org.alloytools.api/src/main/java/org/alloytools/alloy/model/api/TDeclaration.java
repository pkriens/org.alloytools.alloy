package org.alloytools.alloy.model.api;

import java.util.List;

public interface TDeclaration extends TValue {

    boolean var();

    boolean disjNames();

    List<String> names();

    boolean disjValues();

    Multiplicity multiplicity();

    TValue domain();

}
