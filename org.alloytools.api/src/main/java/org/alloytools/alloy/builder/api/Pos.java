package org.alloytools.alloy.builder.api;

import java.util.function.Supplier;

public class Pos<T> implements Supplier<T> {

    final T   value;
    final int start;
    final int stop;

    public Pos(T value, int start, int stop) {
        this.value = value;
        this.start = start;
        this.stop = stop;
    }


    public boolean isPresent() {
        return value != null;
    }

    @Override
    public T get() {
        if (value != null)
            return value;

        throw new IllegalArgumentException("not set");
    }


    public Pos<T> replace(T value) {
        return new Pos<>(value, start, stop);
    }


    public static <T> Pos<T> empty() {
        return new Pos<T>(null, 0, 0);
    }


    public T getOrAbsent(T deflt) {
        return isPresent() ? value : deflt;
    }


    public Pos< ? > merge(Pos< ? > right) {
        // TODO Auto-generated method stub
        return null;
    }
}
