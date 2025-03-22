package org.alloytools.alloy.runtime.relation.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Relation<A, T extends Relation<A, T>> implements Iterable<Tuple<A>> {

	final A[] data;
	final int arity;
	final int size;
	final T THIS;
	final Species<A, T> species;

	@SuppressWarnings("unchecked")
	public Relation(A[] data, int arity, Species<A, T> species) {
		assert data.length % arity == 0;
		this.species = species;
		this.data = data;
		this.arity = arity;
		this.size = data.length / arity;
		THIS = (T) this;
	}

	public T join(T other) {
		assert this.arity * other.arity != 0;

		int arity = this.arity + other.arity - 2;

		assert arity > 0;

		List<A> result = new ArrayList<>();

		for (int i = 0; i < this.data.length && this.data[i] != null; i += this.arity) {
			for (int j = 0; j < other.data.length && other.data[j] != null; i += other.arity) {
				A left = this.last(i);
				A right = other.head(j);
				if (species.equals(left, right)) {
					this.copy(i, this.arity - 1, result);
					other.copy(j + 1, other.arity, result);
				}
			}
		}

		return species.create(result, arity);
	}

	public T union(T other) {
		assert this.arity == other.arity;

		if (this.size == 0)
			return other;
		if (other.size == 0)
			return THIS;

		A[] data = species.create(this.size + other.size);
		System.arraycopy(this.data, 0, data, 0, this.data.length);
		System.arraycopy(other.data, 0, data, this.data.length, other.data.length);

		return species.create(data, arity);
	}

	public T intersection(T other) {
		assert this.arity == other.arity;

		if (this.size == 0 || other.size == 0)
			return none(arity);

		int i = 0, j = 0;
		int maxSize = Math.min(this.size, other.size);
		List<A> result = new ArrayList<>(maxSize);

		while (i < this.size && j < other.size) {
			int comp = species.compare(this.data, i, other.data, j, arity);
			if (comp < 0) {
				i++;
			} else if (comp > 0) {
				j++;
			} else {
				copy(i * arity, arity, result);
				i++;
				j++;
			}
		}
		return species.create(result, arity);
	}

	public T difference(T other) {
		List<A> result = new ArrayList<>(data.length);

		int i = 0, j = 0;
		while (i < this.size && j < other.size) {
			int comp = species.compare(this.data, i, other.data, j, arity);
			if (comp < 0) {
				copy(i * arity, arity, result);
				i++;
			} else if (comp == 0) {
				i++;
				j++;
			} else {
				j++;
			}
		}

		while (i < this.size) {
			copy(i * arity, arity, result);
			i++;
		}
		return species.create(result, arity);
	}

	public T domain() {
		return slice(0, 1);
	}

	public T range() {
		return slice(1, arity - 1);
	}

	public T restrict(Predicate<A> predicate, int... columns) {
		int[] cols = columns.length == 0 ? new int[] { 0 } : columns;
		List<A> result = new ArrayList<>();
		nextTuple: for (int i = 0; i < this.data.length; i += arity) {
			for (int j = 0; j < cols.length; j++) {
				A subject = this.data[i + cols[j]];
				if (predicate.test(subject)) {
					copy(i, arity, result);
					continue nextTuple;
				}
			}
		}
		return species.create(result, arity);
	}

	public T rangeRestriction(Set<A> expected) {
		return restrict(expected::contains, 0);
	}

	public T domainRestriction(Set<A> expected) {
		return restrict(expected::contains, arity - 1);
	}

	public T override(T other, int column) {
		assert this.arity == other.arity;
		List<A> result = new ArrayList<>();
		for (int i = 0; i < this.data.length; i += arity) {
			boolean found = false;
			for (int j = 0; j < other.data.length; j += arity) {
				A thisSubject = this.data[i + 0];
				A otherSubject = other.data[j + 0];
				if (species.equals(thisSubject, otherSubject)) {
					other.copy(j, other.arity, result);
					found = true;
				}
			}
			if (!found) {
				this.copy(i, arity, result);
			}
		}
		return species.create(result, arity);
	}

	public T slice(int start, int length) {
		A[] array = species.newArray().apply(size * length);
		for (int i = 0; i < size; i++) {
			System.arraycopy(this.data, i * arity, array, i * length, length);
		}
		return species.create(array, length);
	}

	public T transpose() {
		List<A> list = Arrays.asList(this.data);
		Collections.reverse(list);
		return species.create(list, arity);
	}

	void copy(int start, int arity, List<A> result) {
		for (int i = 0; i < arity; i++)
			result.add(data[i + start]);
	}

	A head(int start) {
		return null;
	}

	A last(int start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<A>> iterator() {
		return null;
	}

	public T none(int arity) {
		return species.create(species.newArray().apply(0), arity);
	}

	public T none() {
		return none(0);
	}
}
