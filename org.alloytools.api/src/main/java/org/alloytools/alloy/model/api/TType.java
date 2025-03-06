package org.alloytools.alloy.model.api;

public record TType(TSig... sig) {

    public int arity() {
        return sig.length;
    }

    public TSig at(int i) {
        return sig[i];
    }

    public TSig[] slice(int start, int end, TSig... append) {
        if (start < 0 || end > sig.length || start > end) {
            throw new IndexOutOfBoundsException("Invalid slice bounds: start=" + start + ", end=" + end);
        }
        int sliceLength = end - start;
        TSig[] result = new TSig[sliceLength + append.length];
        System.arraycopy(sig, start, result, 0, sliceLength);
        if (append.length > 0) {
            System.arraycopy(append, 0, result, sliceLength, append.length);
        }
        return result;
    }

    public static TType base(TType a, TType b) {
        if (a.arity() != b.arity()) {
            throw new IllegalArgumentException("to find the base types, the arity must be the same");
        }

        TSig[] result = new TSig[a.arity()];
        for (int i = 0; i < a.arity(); i++) {
            result[i] = a.sig[i].common(b.sig[i]);
        }
        return new TType(result);
    }
}

