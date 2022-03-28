/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2022 Bobai Kato. All Rights Reserved.
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

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts three input arguments and returns no result. This is the
 * three-arity specialization of {@link Consumer}. Unlike most other functional interfaces, {@code
 * TriConsumer} is expected to operate via side-effects.
 *
 * @param <X> the type of the first argument to the operation.
 * @param <Y> the type of the second argument to the operation.
 * @param <Z> the type of the third argument to the operation.
 * @author @author <a href="https://github.com/B0BAI">Bobai Kato</a>
 * @see BiConsumer
 * @since 1.0
 */
@FunctionalInterface
public interface TriConsumer<X, Y, Z> {

  /**
   * Returns a composed {@code TriConsumer} that performs, in sequence, this operation followed by
   * the {@code after} operation. If performing either operation throws an exception, it is relayed
   * to the caller of the composed operation. If performing this operation throws an exception, the
   * {@code after} operation will not be performed.
   *
   * @param after the operation to perform after this operation
   * @return a composed {@code TriConsumer} that performs in sequence this operation followed by the
   *     {@code after} operation
   * @throws NullPointerException if {@code after} is null
   */
  default TriConsumer<X, Y, Z> andThen(final TriConsumer<? super X, ? super Y, ? super Z> after) {
    Objects.requireNonNull(after, after.getClass().getSimpleName() + " cannot be null");

    return (a, b, c) -> {
      this.accept(a, b, c);
      after.accept(a, b, c);
    };
  }

  /**
   * Performs this operation on the given argument.
   *
   * @param x the first input argument
   * @param y the second input argument
   * @param z the third input argument
   */
  void accept(final X x, final Y y, final Z z);
}