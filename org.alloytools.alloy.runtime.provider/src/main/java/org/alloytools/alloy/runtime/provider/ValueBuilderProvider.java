package org.alloytools.alloy.runtime.provider;

import java.util.function.BiFunction;

import org.alloytools.alloy.builder.api.ValueBuilder;
import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TValue;
import org.alloytools.alloy.model.api.Where;

public class ValueBuilderProvider implements ValueBuilder {
	final BiFunction<Where,Environment, TValue> op = null;
	final Where where = null;
	
	@Override
	public void setLocation(int start, int end) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TValue resolve(Environment env) {
		return op.apply(where, env);
	}

}
