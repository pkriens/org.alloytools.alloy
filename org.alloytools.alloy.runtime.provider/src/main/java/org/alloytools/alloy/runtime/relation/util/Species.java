package org.alloytools.alloy.runtime.relation.util;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

public record Species<A, T extends Relation<A, T>>(Comparator<A> compare, IntFunction<A[]> newArray,
		BiFunction<A[], Integer, T> factory) {

	public boolean equals(A left, A right) {
		return compare.compare(left, right) == 0;
	}

	public A[] create(int n) {
		return newArray.apply(n);
	}

	public T create(List<A> result, int arity) {
		A[] array = result.toArray(newArray::apply);
		return create(array, arity);
	}

	public T create(A[] array, int arity) {
		A[] cleansed = cleanup(array, arity);
		return factory.apply(cleansed, arity);
	}

	interface IntComparator {
		int compare(int a, int b);
	}

	public A[] cleanup(A[] array, int arity) {
		int[] index = new int[array.length / arity];
		for (int i = 1; i < index.length; i++)
			index[i] = i * arity;

		IntComparator comparator = (a, b) -> {
			for (int i = 0; i < arity; i++) {
				int local = compare.compare(array[a + i], array[b + i]);
				if (local != 0)
					return local;
			}
			return 0;
		};
		boolean changed = quicksort(index, 0, index.length-1, comparator);
		int high = deduplicate(index, comparator);
		if (!changed && high == index.length)
			return array;

		A[] result = newArray.apply(high * arity);
		int out = 0;
		for (int i = 0; i < high; i++) {
			System.arraycopy(array, index[i], result, out, arity);
			out+=arity;
		}
		return result;
	}

	static int deduplicate(int[] index, IntComparator cmp) {
		int out = 0;
		for (int i = 1; i < index.length; i++) {
			if (cmp.compare(index[i], index[out]) != 0) {
				out++;
				index[out] = index[i];
			}
		}
		return out+1;
	}

	static boolean quicksort(int[] index, int low, int high, IntComparator compare) {
		boolean changed = false;
		if (low < high) {
			int pivot = index[high];
			int i = low;
			for (int j = low; j < high; j++) {
				if (compare.compare(index[j], pivot) <= 0) {
					if (i != j) {
						changed = true;
						int temp = index[i];
						index[i] = index[j];
						index[j] = temp;
					}
					i++;
				}
			}
			if (i != high) {
				changed = true;
				int temp = index[i];
				index[i] = index[high];
				index[high] = temp;
			}
			int pivotIndex = i;
			changed |= quicksort(index, low, pivotIndex - 1, compare);
			changed |= quicksort(index, pivotIndex + 1, high, compare);
		}
		return changed;
	}

	public int compare(A[] left, int leftTupleIndex, A[] right, int rightTupleIndex, int arity) {
		int leftStart = leftTupleIndex * arity;
		int rightStart = rightTupleIndex * arity;
		for (int i = 0; i < arity; i++) {
			int local = compare.compare(left[leftStart + i], right[rightStart + i]);
			if (local != 0)
				return local;
		}
		return 0;
	}

}
