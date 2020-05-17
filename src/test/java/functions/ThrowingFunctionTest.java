/*
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

import com.honerfor.cutils.function.LazyFunction;
import com.honerfor.cutils.function.ThrowingFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class ThrowingFunctionTest {

  static final int MOCK_LATENCY = 2000;

  @DisplayName("Expect Idler to memoize values for Suppliers.")
  @ParameterizedTest(name = "{index} =>  value={1}")
  @MethodSource("idlerSupplierOperations")
  void verifyIdlerMemoizedSupplierValues(
      final Function<Integer, Integer> fn, final int input, final int sec) {

    final long startTime = nanoTime();

    final int result = fn.apply(input);

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    Assertions.assertEquals(MOCK_LATENCY + input, result); // result check
    Assertions.assertEquals(sec, executionTime); // check execution time
  }

  private static Stream<Arguments> idlerSupplierOperations() {

    final Function<Integer, Integer> function =
        LazyFunction.of(
            ThrowingFunction.unchecked(
                value -> {
                  final int time = MOCK_LATENCY;
                  try {
                    Thread.sleep(time); // mock operation with high latency
                  } catch (InterruptedException e) {
                  }
                  return time + value;
                }));

    return Stream.of(
        Arguments.of(function, 23, 2),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 24, 2),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 25, 2),
        Arguments.of(function, 24, 0),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 25, 0),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 26, 2),
        Arguments.of(function, 24, 0),
        Arguments.of(function, 23, 0),
        Arguments.of(function, 26, 0),
        Arguments.of(function, 24, 0));
  }
}
