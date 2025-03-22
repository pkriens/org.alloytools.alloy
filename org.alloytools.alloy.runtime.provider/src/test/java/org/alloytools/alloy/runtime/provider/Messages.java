package org.alloytools.alloy.runtime.provider;

import org.alloytools.alloy.runtime.provider.AmbiguousProblem.TValue;

public interface Messages {
	interface ERROR {}
	interface WARN {}
	interface INFO {}
	
	ERROR join_too_few_attributes(TValue left, TValue right);
	
}
