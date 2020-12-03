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

import java.util.Objects;
import java.util.function.Consumer;

/**
 * This is similar to Java {@link Consumer} but the disparity of exceptions throws. {@link Accepter}
 * can be used in place of Java {@link Consumer} for operations that will throw an {@link
 * Exception}. {@link Accepter} can only accepts a single input argument and returns no result.
 * Unlike most other functional interfaces, {@code Accepter} is expected to operate via side
 * effects.
 *
 * @param <T> the type of the input to the operation.
 * @author Bobai Kato <https://github.com/09905x0>
 * @see Consumer
 * @since 3.2
 * @since 2.0
 */
@FunctionalInterface
public interface Accepter<T> {

  /**
   * Returns a composed {@code Accepter} that performs, in sequence, this operation followed by the
   * {@code after} operation. If performing either operation throws an exception, it is relayed to
   * the caller of the composed operation. If performing this operation throws an exception, the
   * {@code after} operation will not be performed.
   *
   * @param after the operation to perform after this operation
   * @return a composed {@code Consumer} that performs in sequence this operation followed by the
   * {@code after} operation.
   */
  default Accepter<T> andThen(final Accepter<? super T> after) {
    Objects.requireNonNull(after, after.getClass().getSimpleName() + " cannot be null");
    return (final T t) -> {
      try {
        this.accept(t);
      } catch (Exception ex) {
        Accepter.sneakyThrow(ex);
      }
      after.accept(t);
    };
  }

  @SuppressWarnings("unchecked")
  static <T extends Exception> void sneakyThrow(final Exception ex) throws T {
    throw (T) ex;
  }

  /**
   * Performs this operation on the given argument.
   *
   * @param t the input argument.
   * @throws Exception throws accepter exception
   */
  void accept(T t) throws Exception;
}
