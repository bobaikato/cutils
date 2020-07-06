/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2020 Honerfor, Inc. All Rights Reserved.
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

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Like the {@link LazyFunction} the {@link LazyUnaryOperator} can be used when the function
 * Argument and Return Type are same. {@link LazyUnaryOperator} is intended to be memorized and
 * idempotent for every parameter. This can be very useful for operations with high latency whose
 * result are used multitudinous. .The result of every function operation are store and retrieves if
 * the same parameter is passed instead of computing again. Every operation is performed once with
 * unique parameters and the result and served whenever needed.
 *
 * @param <T> the result type
 * @author B0BAI
 * @see Function
 * @see LazyFunction
 * @since 5.0
 */
public final class LazyUnaryOperator<T> implements UnaryOperator<T>, Serializable {
  private static final long serialVersionUID = 9181168161835004440L;

  private final transient Function<? super T, ? extends T> state;

  /**
   * Sealed.
   *
   * @param operator argument, instance of {@link UnaryOperator}
   */
  private LazyUnaryOperator(final UnaryOperator<T> operator) {
    this.state = LazyFunction.of(operator);
  }

  /**
   * Take the operator argument of of {@link UnaryOperator} type.
   *
   * @param <T> the type parameter
   * @param operator the function, of {@link UnaryOperator} type
   * @return the function, an instance of {@link Function} type.
   */
  public static <T> UnaryOperator<T> of(final UnaryOperator<T> operator) {
    Objects.requireNonNull(operator, "operator cannot be null");
    return new LazyUnaryOperator<>(operator);
  }

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @return the function result
   */
  @Override
  public T apply(final T t) {
    return this.state.apply(t);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof LazyUnaryOperator) {
      final LazyUnaryOperator<?> that = (LazyUnaryOperator<?>) o;
      return state.equals(that.state);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(state);
  }
}
