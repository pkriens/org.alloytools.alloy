package org.alloytools.alloy.runtime.provider;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Test;

import aQute.lib.collections.MultiMap;

public class AmbiguousProblem {
	
	interface TSig extends TAttribute {}
	
	interface TAttribute {
	}
	
	static class TType {
		final TAttribute[] attribute; 
		
		public TType(TAttribute ... attributes) {
			attribute = attributes;
		}
		
		public int arity() {
			return attribute.length;
		}
	}
	
	interface TValue {
		TType type();
	}

	interface TValueBuilder {
		List<TValue> candidates(Env env);
	}

	interface Env {
		List<Def> resolve(String name);
	}

	record Number(TType type, Integer number) implements TValue {
		@Override
		public final String toString() {
			return Integer.toString(number);
		}
		
	}

	record Join(TType type, TValue left, TValue right) implements TValue {

		@Override
		public final String toString() {
			return "(. " + left + " " + right + ")";
		}
	}

	record Union(TType type, TValue left, TValue right) implements TValue {

		@Override
		public final String toString() {
			return "(+ " + left + " " + right + ")";
		}
	}

	record Value(ValDef val) implements TValue {
		@Override
		public TType type() {
			return val.type;
		}

		@Override
		public final String toString() {
			return "(value " + val.name + ":" + val.id + ")";
		}
	}

	record Error(String explication) implements TValue {
		@Override
		public TType type() {
			return null;
		}

		@Override
		public final String toString() {
			return "!" + explication + "!";
		}
	}

	record Fun(FunDef fun, TValue... values) implements TValue {

		@Override
		public TType type() {
			return fun.type;
		}

		public static TValue make(FunDef def, TValue... parms) {
			return new Fun(def, parms);
		}

		@Override
		public final String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("(call ").append(" ").append(fun.name).append(":").append(fun.id).append("/").append(fun.arity());
			for (TValue tv : values) {
				sb.append(" ").append(tv);
			}
			sb.append(")");
			return sb.toString();
		}
	}

	interface Def {
		String name();

		TType type();
	}

	record FunDef(TType type, String name, int id, String... argsTypes) implements Def, TValue {

		public int arity() {
			return argsTypes.length;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(name).append("(");
			String comma = "";
			for (String type : argsTypes) {
				sb.append(comma).append(type);
				comma = ",";
			}
			sb.append(")");
			return sb.toString();
		}
	}

	record ValDef(TType type, String name, int id) implements Def, TValue {
	}

	static class VB implements TValueBuilder {

		final TValueBuilder body;
		Supplier<String> display;

		VB(TValueBuilder body) {
			this.body = body;
		}

		@Override
		public List<TValue> candidates(Env env) {
			return body.candidates(env);
		}

		VB display(String format, Object... args) {
			this.display = () -> {
				Object[] args2 = toStrings(args);
				return String.format(format, args2);
			};
			return this;
		}

		private Object[] toStrings(Object[] args) {
			for (int i = 0; i < args.length; i++) {
				args[i] = toString(args[i]);
			}
			return args;
		}

		private Object toString(Object object) {
			if (object == null)
				return "null";

			if (object.getClass().isArray()) {
				int l = Array.getLength(object);
				StringBuilder sb = new StringBuilder();
				String del = "";
				for (int i = 0; i < l; i++) {
					sb.append(del).append(Array.get(object, i));
					del = ",";
				}
				return sb.toString();
			}
			return object;
		}

		@Override
		public String toString() {
			return display == null ? super.toString() : display.get();
		}
	}

	static class MB implements Env {
		final MultiMap<String, Def> funs = new MultiMap<>();
		final AtomicInteger id = new AtomicInteger(100);

		TValueBuilder call(FunDef def, TValueBuilder... args) {
			String foo=null;
			
			switch(foo) {
			case "abc" when foo!= null -> {}
			}
			return arguments(parms -> Fun.make(def, parms), args).display("%s/%d(%s)", def.name, def.argsTypes.length,
					args);
		}

		VB box(TValueBuilder outer, TValueBuilder... args) {
			if (args.length == 0 && outer instanceof VB vb)
				return vb;

			TValueBuilder[] builders = concat(outer, args);
			return arguments(parms -> {
				TValue rover = null;
				for (TValue t : parms) {
					if (rover == null)
						rover = t;
					else
						rover = join(t, rover);
				}
				return rover;
			}, builders).display("%s[%s]", outer, args);
		}

		private TValue join(TValue left, TValue right) {
			TType lt = left.type();
			TType rt = right.type();
			//TType type = type(0,lt.arity()-1,lt, 1, rt.arity());
			
		}

		VB reference(TValueBuilder left, String qname, TValueBuilder... arguments) {
			VB vb = new VB(env -> {
				List<TValueBuilder> builders = new ArrayList<>();
				for (Def def : env.resolve(qname)) {
					if (def instanceof FunDef fd) {
						List<TValue> values = functions(env, fd, left, arguments);
						builders.add(x -> values);
					} else if (def instanceof ValDef vd) {
						VB value = singleton(new Value(vd));
						if (left != null) {
							value = join(left, value);
						}
						value = box(value, arguments);
						builders.add(value);
					} else
						assert false : "expected no other types " + def;
				}
				return builders.stream().flatMap(b -> b.candidates(env).stream()).toList();
			});
			int n = left == null ? 0 : 1;
			n += arguments.length == 0 ? 0 : 2;
			switch (n) {
			case 0 -> vb.display("%s", qname);
			case 1 -> vb.display("%s.%s", left, qname);
			case 2 -> vb.display("%s[%s]", qname, arguments);
			case 4 -> vb.display("%s.%s[%s]", left, qname, arguments);
			}
			return vb;
		}

		VB number(int number) {
			return new VB(env -> Collections.singletonList(new Number("I", number))).display("%d", number);
		}

		VB join(TValueBuilder left, TValueBuilder right) {
			return binary(left, right, Join::make).display("%s.%s", left, right);
		}

		VB union(TValueBuilder left, TValueBuilder right) {
			return binary(left, right, Union::make).display("%s+%s", left, right);
		}

		TValueBuilder value(ValDef value) {
			return singleton(new Value(value)).display("%s", value);
		}

		private VB singleton(TValue value) {
			return new VB(env -> Arrays.asList(value));
		}

		private TValueBuilder[] concat(TValueBuilder head, TValueBuilder[] tail) {
			TValueBuilder[] result = new TValueBuilder[tail.length + 1];
			System.arraycopy(tail, 0, result, 1, tail.length);
			result[0] = head;
			return result;
		}

		private List<TValue> functions(Env env, FunDef def, TValueBuilder this_, TValueBuilder... arguments) {
			List<TValue> result = new ArrayList<>();
			
			if (def.arity() == 0) {
				TValueBuilder expr;
				if (this_ != null)
					expr = box(join(this_, call(def)), arguments);// v . f/0 [ p ]
				else
					expr = box(call(def), arguments); // f/0 [ p ]

				return expr.candidates(env);
			} else {
				if (def.arity() == arguments.length ) {
					TValueBuilder expr;
					if (this_ != null)
						expr = join(this_, call(def, arguments));
					else
						expr = call(def, arguments);

					result.addAll(expr.candidates(env));
				} else if (def.arity() == arguments.length + 1 && this_ != null) {
					TValueBuilder expr;
					expr = call(def, concat(this_, arguments));
					result.addAll(expr.candidates(env));
				}
			}
			// add an error block for diagnosis?
			// result.add( new Error("no such method found"));
			return result;
		}

		private VB binary(TValueBuilder left, TValueBuilder right, BiFunction<TValue, TValue, TValue> factory) {
			return new VB(env -> {
				List<TValue> candidates = new ArrayList<>();
				for (TValue[] pair : permutate(env, left, right)) {
					TValue l = pair[0];
					TValue r = pair[1];
					TValue result = factory.apply(l, r);
					candidates.add(result);
				}
				return candidates;
			});
		}

		private static VB arguments(Function<TValue[], TValue> factory, TValueBuilder... arguments) {
			return new VB(env -> {
				List<TValue> result = new ArrayList<>();
				for (TValue[] args : permutate(env, arguments)) {
					TValue apply = factory.apply(args);
					result.add(apply);
				}
				return result;
			});
		}

		private static List<TValue[]> permutate(Env env, TValueBuilder... columns) {
			TValue[] currentCombination = new TValue[columns.length];
			List<TValue[]> result = new ArrayList<>();
			if (columns.length == 0) {
				result.add(currentCombination);
				return result;
			}

			generatePermutations(env, columns, 0, currentCombination, result);
			return result;
		}

		static <T> void generatePermutations(Env env, TValueBuilder[] columns, int depth, TValue[] current,
				List<TValue[]> result) {
			if (depth == columns.length) {
				result.add(Arrays.copyOf(current, current.length));
				return;
			}

			for (TValue item : columns[depth].candidates(env)) {
				current[depth] = item;
				generatePermutations(env, columns, depth + 1, current, result);
			}
		}

		@Override
		public List<Def> resolve(String name) {
			return funs.getOrDefault(name, Collections.emptyList());
		}

		public void fun(String name, String type, String... argTypes) {
			funs.add(name, new FunDef(type, name, id.getAndIncrement(), argTypes));
		}

		public void val(String name, String type) {
			funs.add(name, new ValDef(type, name, id.getAndIncrement()));
		}
	}

	public void testPermutate() {

	}

	@Test
	public void test() {
		MB mb = new MB();
		mb.val("foo", "I");
		mb.fun("foo", "I");
		mb.fun("foo", "I", "I");
		TValueBuilder a = mb.reference(null, "foo", mb.number(1), mb.number(2));
		System.out.println("a= " + a + " " + a.candidates(mb));
		TValueBuilder b = mb.reference(a, "foo");
		System.out.println("b= " + b + " " + b.candidates(mb));
		List<TValue> candidates = b.candidates(mb);
		System.out.println(candidates.stream().map(Object::toString).collect(Collectors.joining("\n")));
		

	}

}
