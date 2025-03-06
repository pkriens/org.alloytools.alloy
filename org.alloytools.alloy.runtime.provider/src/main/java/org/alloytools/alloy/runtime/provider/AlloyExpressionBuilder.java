package org.alloytools.alloy.runtime.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.alloytools.alloy.ast.api.AST;
import org.alloytools.alloy.builder.api.Pos;
import org.alloytools.alloy.builder.api.ValueBuilder;
import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TType;
import org.alloytools.alloy.model.api.TValue;

public class AlloyExpressionBuilder {

	public interface Candidate {
		TType type();

		TValue resolve();
	}

	interface Env {
		List<Candidate> resolve(String qname);

		Expression join(Expression left, Expression right);

		Expression call(Candidate candidate, Expression... args);

		Expression error(String msg, Candidate... candidates);

		Expression literal(Candidate candidate);

	}

	static interface Expression {
		public TType type();
	}

	static class VB implements ValueBuilder {
		VB a, b;

		VB(ValueBuilder object, ValueBuilder object2) {
			// TODO Auto-generated constructor stub
		}

		public TValue resolve(Environment env) {
			Supplier<TValue> factory = null;

			for (Candidate ac : a.candidates(env)) {
				Collection<Candidate> l = b == null ? Collections.singleton(null) : b.candidates(env);
				for (Candidate bc : l) {
					Supplier<TValue> producer = produce(ac, bc);
					if (producer != null) {
						if (factory != null) {
							throw new IllegalStateException("ambiguous");
						} else {
							factory = producer;
						}
					}
				}
			}
			if (factory != null)
				return factory.get();
			else
				throw new IllegalStateException("no path found");
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////

	public ValueBuilder number(Pos<Integer> value) {
		return env -> new AST.Number(env.INT(), value);
	}

	public ValueBuilder qname(Pos<String> qname) {
		// TODO Auto-generated method stub
		return null;
	}

	public ValueBuilder union(ValueBuilder left, ValueBuilder right) {
	
		return (env) -> resolve2(env, left, right, (a,b) -> {
			TType ta = a.type();
			TType tb = b.type();
			if ( ta.arity() == tb.arity()) {
				return () -> new AST.Union.create(TType.base(ta,tb), a.resolve(), b.resolve());
			} else {
				return null;
			}
		});
	}

	private TValue resolve2(Environment env, ValueBuilder left, ValueBuilder right, BiFunction<Candidate,Candidate,Supplier<TValue>> factory ) {
		// TODO Auto-generated method stub
		return null;
	}

	void test() {

	}
}