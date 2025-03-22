package org.alloytools.alloy.runtime.provider;

import java.util.Iterator;
import java.util.Optional;

import org.alloytools.alloy.model.api.TSig;
import org.alloytools.alloy.model.api.TType;

public class InternalType implements TType {
	
	record TupleType(TSig...sigs) implements TTupleType{

		@Override
		public Iterator<TSig> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TSig[] get() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TSig get(int i) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int arity() {
			// TODO Auto-generated method stub
			return 0;
		}} 

	@Override
	public Iterator<TTupleType> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int arity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TType join(TType other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TType product(TType other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSubSetOf(TType other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<String> getError() {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean isBoolean() {
		// TODO Auto-generated method stub
		return false;
	}

}
