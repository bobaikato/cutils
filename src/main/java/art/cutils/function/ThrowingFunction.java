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
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * When you find yourself in a situation where you need to use a method ( that throws an exception)
 * withing Lambda, this class is what you need to achieve your goal without Java complaining.
 *
 * <p>The class Represents a function that accepts one argument and produces a result. whose
 * functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @since 1.0
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

  /**
   * Uncheck method which will take operation that will throw Exception.
   *
   * @param function Variable of {@link ThrowingFunction}
   * @param <T>      the type of the input to the function
   * @param <R>      the type of the result of the function
   * @return A {@link Function}
   */
  static <T, R> @NotNull Function<T, R> unchecked(final ThrowingFunction<T, R> function) {
    Objects.requireNonNull(function, function.getClass().getSimpleName() + " cannot be null");
    return argument -> {
      try {
        return function.apply(argument);
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

  /**
   * Applies the function to the given input.
   *
   * @param t the input to the function
   * @return the result of applying the function to the input
   * @throws Exception if an error occurs during function execution
   */
  R apply(T t) throws Exception;
}