package org.alloytools.alloy.runtime.provider;

import java.util.function.Function;
import java.util.function.Supplier;

import org.alloytools.alloy.model.api.Environment;

public class LazyResolve<T> implements Supplier<T> {
	T value;
	Function<Environment, T> initializer;

	public LazyResolve(Function<Environment, T> initializer) {
		this.initializer = initializer;
	}

	@Override
	public T get() {
		assert value != null;
		return value;
	}

	public void resolve(Environment t) {
		assert value == null;
		value = initializer.apply(t);
		initializer = null;
	}

}
