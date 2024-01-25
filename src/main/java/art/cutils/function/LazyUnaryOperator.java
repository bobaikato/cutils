/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2023 Bobai Kato. All Rights Reserved.
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

package art.cutils.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Like the {@link LazyFunction} the {@link LazyUnaryOperator} can be used when the function
 * Argument and Return Type are same. {@link LazyUnaryOperator} is intended to be memorized and
 * idempotent for every parameter. This can be very useful for operations with high latency whose
 * result are used multitudinous. .The result of every function operation are store and retrieves if
 * the same parameter is passed instead of computing again. Every operation is performed once with
 * unique parameters and the result and served whenever needed.
 *
 * @param <T> the result type
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @see Function
 * @see LazyFunction
 * @since 1.0
 */
public final class LazyUnaryOperator<T> implements UnaryOperator<T>, Serializable {
  private static final long serialVersionUID = 9181168161835004440L;

  private final transient Function<T, T> state;

  /**
   * Sealed.
   *
   * @param operator argument, instance of {@link UnaryOperator}
   */
  private LazyUnaryOperator(final UnaryOperator<T> operator) {
    this.state = LazyFunction.of(operator);
  }

  /**
   * Creates a new instance of {@link UnaryOperator} that lazily applies the given {@link
   * UnaryOperator}.
   *
   * @param operator the {@link UnaryOperator} to be applied lazily
   * @param <T> the type of the argument and result of the {@link UnaryOperator}
   * @return a new instance of {@link UnaryOperator} that applies the given {@link UnaryOperator}
   *     lazily
   * @throws NullPointerException if the {@code operator} is null
   */
  @Contract("_ -> new")
  public static <T> @NotNull UnaryOperator<T> of(final UnaryOperator<T> operator) {
    Objects.requireNonNull(operator, "operator cannot be null");
    return new LazyUnaryOperator<>(operator);
  }

  /**
   * Applies the function to the given argument.
   *
   * @param t the argument to apply the function to
   * @return the result of applying the function to the argument
   * @throws NullPointerException if the argument is null
   */
  @Override
  public T apply(final T t) {
    return this.state.apply(t);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state);
  }

  @Override
  @Contract(value = "null -> false", pure = true)
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
}
