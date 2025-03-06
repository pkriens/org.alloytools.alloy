package org.alloytools.alloy.builder.api;

import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TFormula;

public interface FormulaBuilder extends ExprBuilder {

    TFormula resolve(Environment env);
}

