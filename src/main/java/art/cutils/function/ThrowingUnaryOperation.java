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

import java.util.Objects;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an operation on a single operand that produces a result of the same type as its
 * operand. This is a specialization of {@code Function} for the case where the operand and result
 * are of the same type.
 *
 * <p>When you find yourself in a situation where you need to use a function ( that throws an
 * exception), this class is what you need to achieve your goal without Java complaining.
 *
 * <p>The class Represents a function that accepts one argument and produces a result. whose
 * functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @see ThrowingFunction
 * @see UnaryOperator
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @since 1.0
 */
@FunctionalInterface
public interface ThrowingUnaryOperation<T> extends ThrowingFunction<T, T> {

  /**
   * Returns a {@link UnaryOperator} that simply returns its input. This method is useful when you
   * need a function that does not transform the input in any way.
   *
   * @param <T> the type of the input and output of the UnaryOperator
   * @return a UnaryOperator that returns its input unchanged
   */
  @Contract(pure = true)
  static <T> @NotNull UnaryOperator<T> identity() {
    return t -> t;
  }

  /**
   * Applies the given throwing unary operation to an argument. If an exception is thrown during the
   * operation, it is caught and rethrown as a unchecked exception.
   *
   * @param <T> the type of the argument and result of the operation
   * @param operator the throwing unary operation to be applied
   * @return a unary operator that applies the throwing unary operation to the argument and returns
   *     the result, or throws an unchecked exception if an exception occurs during the operation
   * @throws NullPointerException if the operator is null
   */
  @Contract(pure = true)
  static <T> @NotNull UnaryOperator<T> unchecked(final ThrowingUnaryOperation<T> operator) {
    Objects.requireNonNull(operator, "operator cannot be null");
    return argument -> {
      try {
        return operator.apply(argument);
      } catch (Exception ex) {
        return sneakyThrow(ex);
      }
    };
  }

  /**
   * Throws a given exception without requiring it to be declared in the method signature.
   *
   * @param ex the exception to throw
   * @param <T> the specific type of exception to throw
   * @param <R> the return type of the method
   * @return does not actually return a value (throws an exception instead)
   * @throws T the given exception, cast to the specific type
   */
  @SuppressWarnings("unchecked")
  @Contract(value = "_ -> fail", pure = true)
  static <T extends Exception, R> R sneakyThrow(final Exception ex) throws T {
    throw (T) ex;
  }
}