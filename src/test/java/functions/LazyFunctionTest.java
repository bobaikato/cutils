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

package functions;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.honerfor.cutils.function.LazyFunction;
import com.honerfor.cutils.function.ThrowingFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class LazyFunctionTest {

  static final int MOCK_LATENCY = 2000;

  private static Stream<Arguments> lazyFunctionOperations() {

    final Function<Integer, Integer> function =
        LazyFunction.of(
            ThrowingFunction.unchecked(
                value -> {
                  Thread.sleep(MOCK_LATENCY); // mock operation with high latency
                  return MOCK_LATENCY + value;
                }));

    return Stream.of(
        Arguments.of(function, 23, 2),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 25, 2),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 25, 0),
        Arguments.of(function, 23, 0));
  }

  @DisplayName("Expect Lazy Function to memoize values for Functions of same argument.")
  @ParameterizedTest(name = "{index} =>  input={1}  second={2}")
  @MethodSource("lazyFunctionOperations")
  void verifyLazyFunctionMemoizedValues(
      final Function<Integer, Integer> fn, final int input, final int sec) {

    final long startTime = nanoTime();

    final int result = fn.apply(input);

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    assertEquals(MOCK_LATENCY + input, result); // result check
    assertEquals(sec, executionTime); // check execution time
  }

  @Test
  public void equalsAndHashCodeContractToBeValid() {
    final Function<String, String> f1 = LazyFunction.of(value -> value.replace("o", "0"));
    final Function<String, String> f2 = f1;

    assertEquals(f1, f2);
    assertEquals(f1.hashCode(), f2.hashCode());
  }

  @Test
  public void equalsAndHashCodeContractToBeInvalid() {
    final Function<String, String> f1 = LazyFunction.of(value -> value.replace("o", "0"));
    final Function<String, String> f2 = LazyFunction.of(value -> value.replace("o", "0"));

    assertNotEquals(f1, f2);
    assertNotEquals(f1, "");
    assertNotEquals(f1.hashCode(), f2.hashCode());
  }
}
