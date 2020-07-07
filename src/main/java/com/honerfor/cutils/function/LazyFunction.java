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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * The {@link LazyFunction} is intended to be memorized and idempotent for every parameter. This can
 * be very useful for operations with high latency whose result are used multitudinous. .The result
 * of every function operation are store and retrieves if the same parameter is passed instead of
 * computing again. Every operation is performed once with unique parameters and the result and
 * served whenever needed.
 *
 * @param <T> the result type
 * @param <R> the return type
 * @author B0BAI <https://github.com/b0bai>
 * @see Function
 * @since 5.0
 */
public final class LazyFunction<T, R> implements Function<T, R>, Serializable {
  private static final long serialVersionUID = 398334400292617685L;

  private final transient Function<? super T, ? extends R> function;

  /**
   * Store the current result of function by the the argument provided.
   *
   * @since 5.0
   */
  private final transient Map<T, R> store = new ConcurrentHashMap<>();

  /**
   * Sealed.
   *
   * @param function argument, instance of {@link Function}
   */
  private LazyFunction(final Function<? super T, ? extends R> function) {
    this.function = function;
  }

  /**
   * Take the function operation of {@link Function} type.
   *
   * @param <T> the type parameter
   * @param <R> the return type parameter
   * @param function the function, of {@link Function} type
   * @return the function, an instance of {@link Function} type.
   */
  public static <T, R> Function<T, R> of(final Function<? super T, ? extends R> function) {
    Objects.requireNonNull(function, "function cannot be null");
    return new LazyFunction<>(function);
  }

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @return the function result
   */
  @Override
  public R apply(final T t) {
    if (this.store.containsKey(t)) {
      return this.store.get(t);
    }
    final R value = this.function.apply(t);
    this.store.putIfAbsent(t, value);
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof LazyFunction) {
      final LazyFunction<?, ?> that = (LazyFunction<?, ?>) o;
      return function.equals(that.function) && store.equals(that.store);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(function, store);
  }
}
