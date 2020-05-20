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

import com.honerfor.cutils.function.Dealer;
import com.honerfor.cutils.function.Idler;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class IdlerTest {

  static final int MOCK_LATENCY = 2000;

  @DisplayName("Expect Idler to memoize values for Dealers.")
  @ParameterizedTest(name = "{index} =>  seconds={1}")
  @MethodSource("idlerDealerOperations")
  void verifyIdlerMemoizedDealerValues(final Dealer<Integer> dealer, final long sec)
      throws Exception {
    final long startTime = nanoTime();

    final int result = dealer.deal();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    Assertions.assertEquals(MOCK_LATENCY, result); // result check
    Assertions.assertEquals(sec, executionTime); // check execution time
  }

  private static Stream<Arguments> idlerDealerOperations() {

    final Dealer<Integer> dealer =
        Idler.deal(
            () -> {
              final int time = MOCK_LATENCY;
              Thread.sleep(time); // mock operation with high latency
              return time;
            });

    return Stream.of(
        Arguments.of(dealer, 2),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0));
  }

  @DisplayName("Expect Idler to memoize values for Suppliers.")
  @ParameterizedTest(name = "{index} =>  second={1}")
  @MethodSource("idlerSupplierOperations")
  void verifyIdlerMemoizedSupplierValues(final Supplier<Integer> supplier, final long sec) {
    final long startTime = nanoTime();

    final int result = supplier.get();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    Assertions.assertEquals(MOCK_LATENCY, result); // result check
    Assertions.assertEquals(sec, executionTime); // check execution time
  }

  private static Stream<Arguments> idlerSupplierOperations() {

    final Supplier<Integer> supplier =
        Idler.supply(
            () -> {
              final int time = MOCK_LATENCY;
              try {
                Thread.sleep(time); // mock operation with high latency
              } catch (InterruptedException e) {
              }
              return time;
            });

    return Stream.of(
        Arguments.of(supplier, 2),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0));
  }
}
