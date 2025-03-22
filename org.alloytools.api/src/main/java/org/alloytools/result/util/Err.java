/*
 * Copyright 2017 René Perschon <rperschon85@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.alloytools.result.util;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * This class represents the Err side of @{link Result}.
 *
 * @param <V> The value type
 */
final class Err<V> implements Result<V> {

    private final String error;

    Err(CharSequence error) {
        this.error = requireNonNull(error.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOk() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isErr() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<V> value() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> error() {
        return Optional.of(error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V unwrap() {
        throw new ResultException(error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V unwrap(CharSequence message) throws ResultException {
        throw new ResultException(message.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V orElse(V orElse) {
        return orElse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V orElseGet(Supplier< ? extends V> orElseSupplier) {
        return orElseSupplier.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R extends Throwable> V orElseThrow(Function< ? super String, ? extends R> throwableSupplier) throws R {
        requireNonNull(throwableSupplier);
        throw requireNonNull(throwableSupplier.apply(error));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Result<U> map(Function< ? super V, ? extends U> mapper) {
        requireNonNull(mapper);
        return asError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<V> mapErr(Function< ? super String, ? extends CharSequence> mapper) {
        return Result.err(mapper.apply(error));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Result<U> flatMap(Function< ? super V, ? extends Result< ? extends U>> mapper) {
        requireNonNull(mapper);
        return asError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<V> recover(Function< ? super String, ? extends V> recover) {
        V v = recover.apply(error);
        return (v != null) ? Result.ok(v) : this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<V> recoverWith(Function< ? super String, ? extends Result< ? extends V>> recover) {
        @SuppressWarnings("unchecked" )
        Result<V> result = (Result<V>) requireNonNull(recover.apply(error));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Consumer< ? super V> ok, Consumer< ? super String> err) {
        requireNonNull(ok);
        err.accept(error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Result<U> asError() {
        @SuppressWarnings("unchecked" )
        Result<U> coerced = (Result<U>) this;
        return coerced;
    }

    @Override
    public String toString() {
        return String.format("Err(%s)", error);
    }
}
