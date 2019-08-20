/*
 * Copyright (C) 2018 — 2019 Honerfor, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.honerfor.cutils.function;

import com.honerfor.cutils.Que;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result.  This is the three-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code TriConsumer} is expected
 * to operate via side-effects.
 *
 * @param <X> the type of the first argument to the operation
 * @param <Y> the type of the second argument to the operation
 * @param <Z> the type of the third argument to the operation
 * @author B0BAI
 * @see BiConsumer
 * @since 2.0
 */
@FunctionalInterface
public interface TriConsumer<X, Y, Z> {

    /**
     * <p> Performs this operation on the given argument.</p>
     *
     * @param x the first input argument
     * @param y the second input argument
     * @param z the third input argument
     */
    void accept(X x, Y y, Z z);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<X, Y, Z> andThen(TriConsumer<? super X, ? super Y, ? super Z> after) {
        return Que.<TriConsumer<X, Y, Z>>run(() -> {
            Objects.requireNonNull(after);
        }).andSupply(() -> (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        });
    }
}
