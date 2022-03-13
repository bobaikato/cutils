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

package functions;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import art.cutils.function.LazyUnaryOperator;
import art.cutils.function.ThrowingUnaryOperation;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class LazyUnaryOperatorTest {

  static final int MOCK_LATENCY = 2000;

  private static @NotNull Stream<Arguments> lazyFunctionOperations() {

    final UnaryOperator<String> function =
        LazyUnaryOperator.of(
            ThrowingUnaryOperation.unchecked(
                value -> {
                  Thread.sleep(MOCK_LATENCY); // mock operation with high latency
                  return MOCK_LATENCY + value;
                }));

    return Stream.of(
        Arguments.of(function, "23", 2),
        Arguments.of(function, "23", 0),
        Arguments.of(function, "24", 2),
        Arguments.of(function, "23", 0),
        Arguments.of(function, "23", 0),
        Arguments.of(function, "25", 2),
        Arguments.of(function, "24", 0),
        Arguments.of(function, "23", 0),
        Arguments.of(function, "25", 0),
        Arguments.of(function, "23", 0),
        Arguments.of(function, "24", 0),
        Arguments.of(function, "23", 0),
        Arguments.of(function, "24", 0));
  }

  @DisplayName("Expect Lazy Function to memoize values for Functions of same argument.")
  @ParameterizedTest(name = "{index} =>  input={1}  second={2}")
  @MethodSource("lazyFunctionOperations")
  void verifyLazyUnaryOperatorValues(
      final @NotNull UnaryOperator<String> operator, final String input, int sec) {

    final long startTime = nanoTime();

    final String result = operator.apply(input);

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    Assertions.assertEquals(MOCK_LATENCY + input, result); // result check
    Assertions.assertEquals(sec, executionTime); // check execution time
  }

  @Test
  void equalsAndHashCodeContractToBeValid() {
    final UnaryOperator<String> o1 = LazyUnaryOperator.of(value -> value.replace("o", "0"));
    final UnaryOperator<String> o2 = o1;

    Assertions.assertEquals(o1, o2);
    Assertions.assertEquals(o1.hashCode(), o2.hashCode());
  }

  @Test
  void equalsAndHashCodeContractToBeInvalid() {
    final UnaryOperator<String> o1 = LazyUnaryOperator.of(value -> value.replace("o", "0"));
    final UnaryOperator<String> o2 = LazyUnaryOperator.of(value -> value.replace("o", "0"));

    assertNotEquals(o1, o2);
    assertNotEquals(o1, "");
    assertNotEquals(o1.hashCode(), o2.hashCode());
  }
}