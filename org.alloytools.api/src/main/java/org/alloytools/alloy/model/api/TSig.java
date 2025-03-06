package org.alloytools.alloy.model.api;

import java.util.Map;
import java.util.Optional;

/**
 * Represents a signature in Alloy.
 */
public interface TSig extends TValue {

    /**
     * The name of the signature
     *
     * @return the name of the signature.
     */
    String name();

    /**
     * The field relations associated with the signature
     *
     * @return the fields
     */
    Map<String,TDeclaration> fields();

    Optional<TFormula> block();

    TSig common(TSig... with);


}
