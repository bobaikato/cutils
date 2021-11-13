/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2021 Bobai Kato. All Rights Reserved.
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

package value;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import art.cutils.value.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Try Operation test.")
final class TryTest {

  @Test
  void testForSuccessTryOperationWithResult() {

    final Try<Integer> convertStringToInteger = Try.of(() -> Integer.parseInt("0025"));

    assertTrue(convertStringToInteger.isSuccess());

    assertFalse(convertStringToInteger.isFailure());

    assertTrue(convertStringToInteger.isResult());

    assertEquals(25, convertStringToInteger.get());

    final Try<Integer> squareRoot = convertStringToInteger.map(result -> (int) Math.sqrt(result));

    assertNotEquals(squareRoot, convertStringToInteger);

    assertTrue(squareRoot.isSuccess());
    assertTrue(squareRoot.isResult());

    assertEquals(5, squareRoot.orElse(24));

    assertEquals(5, squareRoot.orElseGet(() -> 34));

    final Exception ex =
        assertThrows(UnsupportedOperationException.class, convertStringToInteger::getCause);

    assertEquals("Operation was successful, without any exception thrown.", ex.getMessage());
  }

  @Test
  void testForFailedTryOperation() {

    final Try<Integer> convertStringToInteger = Try.of(() -> Integer.parseInt("0o25"));

    assertTrue(convertStringToInteger.isFailure());

    assertFalse(convertStringToInteger.isSuccess());

    assertFalse(convertStringToInteger.isResult());

    final Exception getEx =
        assertThrows(UnsupportedOperationException.class, convertStringToInteger::get);

    assertEquals("No result available, operation failed with an exception.", getEx.getMessage());

    final Exception mapEx =
        assertThrows(
            UnsupportedOperationException.class,
            () -> convertStringToInteger.map(result -> (int) Math.sqrt(result)));

    assertEquals("No result available, operation failed with an exception.", getEx.getMessage());

    assertTrue(convertStringToInteger.getCause() instanceof NumberFormatException);

    assertEquals(25, convertStringToInteger.orElse(25));

    assertEquals(25, convertStringToInteger.orElseGet(() -> 25));

    final Exception throwEx =
        assertThrows(
            RuntimeException.class,
            () -> {
              convertStringToInteger.orElseThrow(
                  () -> {
                    throw new RuntimeException("An exception from orElseThrow.");
                  });
            });
    assertEquals("An exception from orElseThrow.", throwEx.getMessage());
  }

  @Test
  void testForSuccessTryOperationWithoutResult() {

    final Try<?> pause = Try.of(() -> sleep(0));

    assertTrue(pause.isSuccess());

    assertFalse(pause.isFailure());

    assertFalse(pause.isResult());

    final Exception getEx = assertThrows(IllegalStateException.class, pause::get);

    assertEquals("Operation has no result available.", getEx.getMessage());

    final Exception mapEx =
        assertThrows(
            IllegalStateException.class,
            () -> {
              pause.map(Object::toString);
            });

    assertEquals("No result available to map.", mapEx.getMessage());
  }

  @Test
  void testEqualsAndHashCodeContractsForSuccessTryState() {

    final Try<?> t1 = Try.of(() -> {});

    final Object t3 = t1;

    final Try<?> t2 = Try.of(() -> null);

    assertNotEquals(t1, t2);

    assertEquals(t1, t3);

    assertNotEquals(t3, t2);

    assertNotEquals(t2, new Object());

    assertEquals(t1.hashCode(), t3.hashCode());

    assertNotEquals(t1.hashCode(), t2.hashCode());
  }

  @Test
  void testEqualsAndHashCodeContractsForFailedTryState() {
    final Try<?> t1 =
        Try.of(
            () -> {
              Integer.parseInt("2E4");
            });

    final Object t3 = t1;

    final Try<?> t2 = Try.of(() -> Double.parseDouble(null));

    assertNotEquals(t1, t2);

    assertEquals(t1, t3);

    assertNotEquals(t3, t2);

    assertNotEquals(t2, new Object());

    assertEquals(t1.hashCode(), t3.hashCode());

    assertNotEquals(t1.hashCode(), t2.hashCode());
  }

  @Test
  void supplementaryTryTest() {
    final Try<?> successTryWithoutResult =
        Try.of(
            () -> {
              Integer.parseInt("25");
            });

    final Exception ex =
        assertThrows(
            IllegalStateException.class,
            () -> successTryWithoutResult.onSuccess(System.out::println));

    assertEquals("Operation has no result available.", ex.getMessage());

    final Try<?> successTryWithResult = Try.of(() -> Integer.parseInt("25"));

    successTryWithResult.onSuccess(
        result -> {
          assertEquals(25, result);
        });

    successTryWithResult.onSuccessOrElse(
        result -> {
          assertEquals(25, result);
        },
        () -> {});

    successTryWithResult.onSuccessOrElse(
        result -> {
          assertEquals(25, result);
        },
        () -> {});

    successTryWithResult.onSuccessOrElse(
        result -> {
          assertEquals(25, result);
        },
        e -> {
          // Failed with exception available
        });

    final Try<?> failTry =
        Try.of(
            () -> {
              Integer.parseInt("2F");
            });

    failTry.onFailure(
        e -> {
          assertTrue(e.getMessage().contains("For input string: \"2F\""));
        });

    failTry.onFailureOrElse(
        exception -> {
          assertTrue(exception.getMessage().contains("For input string: \"2F\""));
        },
        () -> {
          // success story
        });

    failTry.onFailureOrElse(
        exception -> {
          assertTrue(exception.getMessage().contains("For input string: \"2F\""));
        },
        result -> {
          // success story with result available
        });
  }
}
