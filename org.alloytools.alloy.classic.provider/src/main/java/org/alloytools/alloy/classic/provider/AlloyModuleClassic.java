package org.alloytools.alloy.classic.provider;

import org.alloytools.alloy.model.api.TModule;

import edu.mit.csail.sdg.parser.CompModule;

public interface AlloyModuleClassic extends TModule {

    CompModule getOriginalModule();

}
