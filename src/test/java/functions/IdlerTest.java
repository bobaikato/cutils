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
import static org.junit.jupiter.params.provider.Arguments.of;

import com.honerfor.cutils.function.Dealer;
import com.honerfor.cutils.function.Idler;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class IdlerTest {

  static final int MOCK_LATENCY = 2000;

  static final Supplier<Integer> SUPPLIER =
    () -> {
      final int time = MOCK_LATENCY;
      try {
        Thread.sleep(time); // mock operation with high latency
      } catch (InterruptedException e) {
      }
      return time;
    };

  static final Dealer<Integer> DEALER =
    () -> {
      final int time = MOCK_LATENCY;
      Thread.sleep(time); // mock operation with high latency
      return time;
    };

  private static Stream<Arguments> idlerOperations() {

    final Dealer<Integer> dealer = Idler.deal(DEALER);
    final Supplier<Integer> supplier = Idler.supply(SUPPLIER);

    return Stream.of(
      of(dealer, 2, supplier),
      of(dealer, 0, supplier),
      of(dealer, 0, supplier),
      of(dealer, 0, supplier),
      of(dealer, 0, supplier),
      of(dealer, 0, supplier),
      of(dealer, 0, supplier),
      of(dealer, 0, supplier),
      of(dealer, 0, supplier));
  }

  private static Stream<Arguments> idlerOperationsForBothSupplierAndDealer() {
    final Idler<Integer> idler = Idler.of(SUPPLIER, DEALER);
    return Stream.of(
      of(idler, 4),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0));
  }

  private static Stream<Arguments> idlerOperationsForBothDealerAndSupplier() {
    final Idler<Integer> idler = Idler.of(DEALER, SUPPLIER);
    return Stream.of(
      of(idler, 4),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0),
      of(idler, 0));
  }

  @DisplayName("Expect Idler to memoize values for Dealers.")
  @ParameterizedTest(name = "{index} =>  seconds={1}")
  @MethodSource("idlerOperations")
  void verifyIdlerMemoizedDealerValues(final Dealer<Integer> dealer, final long sec)
    throws Exception {
    final long startTime = nanoTime();
    final int result = dealer.deal();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    assertEquals(MOCK_LATENCY, result); // result check
    assertEquals(sec, executionTime); // check execution time
  }

  @DisplayName("Expect Idler to memoize values for Suppliers.")
  @ParameterizedTest(name = "{index} =>  second={1}")
  @MethodSource("idlerOperations")
  void verifyIdlerMemoizedSupplierValues(
    final Dealer<Integer> dealer, final long sec, final Supplier<Integer> supplier) {
    final long startTime = nanoTime();

    final int result = supplier.get();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    assertEquals(MOCK_LATENCY, result); // result check
    assertEquals(sec, executionTime); // check execution time
  }

  @DisplayName("Expect Idler to memoize values both Suppliers and Dealers.")
  @ParameterizedTest(name = "{index} =>  second={1}")
  @MethodSource("idlerOperationsForBothSupplierAndDealer")
  void verifyIdlerMemoizedSupplierAndDealerValues(Idler<Integer> idler, final long sec)
    throws Exception {
    final long startTime = nanoTime();

    final int supplierResult = idler.get();
    final int dealerResult = idler.deal();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    assertEquals(MOCK_LATENCY, supplierResult); // supplier result check
    assertEquals(MOCK_LATENCY, dealerResult); // dealer result check
    assertEquals(sec, executionTime); // check execution time
  }

  @DisplayName("Expect Idler to memoize values both Dealer and Supplier.")
  @ParameterizedTest(name = "{index} =>  second={1}")
  @MethodSource("idlerOperationsForBothDealerAndSupplier")
  void verifyIdlerMemoizedDealerAndSupplierValues(Idler<Integer> idler, final long sec)
    throws Exception {
    final long startTime = nanoTime();

    final int supplierResult = idler.get();
    final int dealerResult = idler.deal();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    assertEquals(MOCK_LATENCY, supplierResult); // supplier result check
    assertEquals(MOCK_LATENCY, dealerResult); // dealer result check
    assertEquals(sec, executionTime); // check execution time
  }

  @Test
  public void equalsAndHashCodeContractToBeValid() {

    final Supplier<?> idler1 = Idler.supply(() -> "Supplier");
    final Supplier<?> idler2 = idler1;

    assertEquals(idler1, idler1);
    assertEquals(idler2, idler2);
    assertEquals(idler1, idler2);

    assertEquals(idler2.hashCode(), idler1.hashCode());
  }

  @Test
  public void equalsAndHashCodeContractToBeInvalid() {

    final Supplier<?> supplier = Idler.supply(() -> "Supplier");
    final Dealer<?> dealer = Idler.deal(() -> "Dealar");

    assertNotEquals(supplier, dealer);
    assertNotEquals(supplier, new String());
    assertNotEquals(dealer, "");

    assertNotEquals(supplier.hashCode(), dealer.hashCode());
  }
}
