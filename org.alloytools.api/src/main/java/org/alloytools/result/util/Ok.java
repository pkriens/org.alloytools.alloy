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
 * This class represents the Ok side of @{link Result}.
 *
 * @param <V> The value type
 */
final class Ok<V> implements Result<V> {

    private final V value;

    /**
     * Constructor.
     *
     * @param value the value
     */
    Ok(V value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOk() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isErr() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<V> value() {
        return Optional.ofNullable(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> error() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V unwrap() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V unwrap(CharSequence message) throws ResultException {
        requireNonNull(message);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V orElse(V orElse) {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V orElseGet(Supplier< ? extends V> orElseSupplier) {
        requireNonNull(orElseSupplier);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R extends Throwable> V orElseThrow(Function< ? super String, ? extends R> throwableSupplier) throws R {
        requireNonNull(throwableSupplier);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Result<U> map(Function< ? super V, ? extends U> mapper) {
        return Result.ok(mapper.apply(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<V> mapErr(Function< ? super String, ? extends CharSequence> mapper) {
        requireNonNull(mapper);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Result<U> flatMap(Function< ? super V, ? extends Result< ? extends U>> mapper) {
        @SuppressWarnings("unchecked" )
        Result<U> result = (Result<U>) requireNonNull(mapper.apply(value));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<V> recover(Function< ? super String, ? extends V> recover) {
        requireNonNull(recover);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<V> recoverWith(Function< ? super String, ? extends Result< ? extends V>> recover) {
        requireNonNull(recover);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Consumer< ? super V> ok, Consumer< ? super String> err) {
        requireNonNull(err);
        ok.accept(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Result<U> asError() {
        throw new ResultException("Not an Err value");
    }

    @Override
    public String toString() {
        return String.format("Ok(%s)", value);
    }
}
