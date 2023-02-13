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

package value;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import art.cutils.value.Try;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Try Operation test.")
final class TryTest {

  @Test
  void testForSuccessTryOperationWithResult() {

    final Try<Integer> convertStringToInteger = Try.of(() -> Integer.parseInt("0025"));

    assertTrue(convertStringToInteger.isSuccess());

    assertFalse(convertStringToInteger.isFailure());

    assertEquals(convertStringToInteger.filter(Objects::nonNull), convertStringToInteger);

    assertNotEquals(convertStringToInteger.filter(Objects::isNull), convertStringToInteger);

    assertTrue(convertStringToInteger.isResult());

    assertEquals(25, convertStringToInteger.get());

    final Try<Integer> squareRoot = convertStringToInteger.map(result -> (int) Math.sqrt(result));

    assertNotEquals(squareRoot, convertStringToInteger);

    assertTrue(squareRoot.isSuccess());
    assertTrue(squareRoot.isResult());

    assertEquals(5, squareRoot.orElseGet(24));

    assertEquals(5, squareRoot.orElseGet(() -> 34));

    assertNull(convertStringToInteger.getCause());
  }

  @Test
  void testTryMapperReturningOriginalFailureCause() {
    Try.of(() -> Integer.parseInt("0O25"))
        .map(
            result -> {
              assertEquals(25, result);
              return (int) Math.sqrt(result);
            })
        .peek(
            result -> {
              // ignored
            })
        .onFailure(
            cause -> {
              assertEquals("For input string: \"0O25\"", cause.getMessage());
            });
  }

  @Test
  void testTryMappingWithoutResult() {
    Try.of(
            () -> {
              Integer.parseInt("025");
            })
        .map(result -> Integer.parseInt(String.valueOf(result)))
        .onFailure(
            cause -> {
              assertNotNull(cause);
              assertEquals("For input string: \"null\"", cause.getMessage());
            });
  }

  @Test
  void testForFailedTryOperation() {

    final Try<Integer> convertStringToInteger = Try.of(() -> Integer.parseInt("0o25"));

    assertTrue(convertStringToInteger.isFailure());

    assertFalse(convertStringToInteger.isSuccess());

    assertFalse(convertStringToInteger.isResult());

    assertEquals(convertStringToInteger.filter(Objects::nonNull), convertStringToInteger);

    assertTrue(convertStringToInteger.getCause() instanceof NumberFormatException);

    assertEquals(25, convertStringToInteger.orElseGet(25));

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

    final Exception throwEx2 =
        assertThrows(
            RuntimeException.class,
            () -> {
              convertStringToInteger.orElseThrow(new Exception("An exception from orElseThrow."));
            });

    assertEquals("java.lang.Exception: An exception from orElseThrow.", throwEx2.getMessage());
  }

  @Test
  void testTryPeekSuccess() {
    final int sameResult =
        Try.of(() -> Integer.parseInt("0025"))
            .peek(result -> assertEquals(25, result))
            .onFailure(
                cause -> {
                  // ignored
                })
            .get();

    assertEquals(25, sameResult);
  }
  // test PeekFailure
  @Test
  void testTryPeekFailureAffectingTryState() {
    final int finalResult =
        Try.of(() -> Integer.parseInt("030"))
            .peek(result -> Integer.parseInt("o" + result))
            .onFailure(
                cause -> {
                  assertEquals("For input string: \"o30\"", cause.getMessage());
                })
            .orElseGet(100);

    assertEquals(100, finalResult);
  }

  @Test
  void testOptionalReturnValue() {
    final Try<Integer> convertStringToInteger = Try.of(() -> Integer.parseInt("25"));

    assertTrue(convertStringToInteger.isResult());
    assertTrue(convertStringToInteger.getOptional().isPresent());

    convertStringToInteger
        .getOptional()
        .ifPresent(
            result -> {
              assertEquals(25, result);
            });
  }

  @Test
  void testForSuccessTryOperationWithoutResult() {

    final Try<?> pause =
        Try.of(
            () -> {
              sleep(0);
              System.out.println();
            });

    assertTrue(pause.isSuccess());

    assertFalse(pause.isFailure());

    assertFalse(pause.isResult());

    assertNull(pause.get());
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

    Try.of(() -> Integer.parseInt("25"))
        .onSuccess(result -> assertEquals(25, result))
        .onSuccess(
            () -> {
              // Execute some code on success
            })
        .onFailure(
            cause -> {
              // exception here if Try fails.
            });

    Try.of(
            () -> {
              Integer.parseInt("2F");
            })
        .onFailure(e -> assertTrue(e.getMessage().contains("For input string: \"2F\"")))
        .onFailure(
            () -> {
              System.out.println("Exception thrown");
              // Execute some code on failure
            });
  }

  @Test
  void test_try_with_result_states() {
    Try<Integer> t1 = Try.of(() -> Integer.parseInt("25")).filter(Objects::nonNull);

    assertTrue(t1.isActive());

    t1.filter(result -> result > 0);
    assertTrue(t1.isActive());

    Try<Integer> t2 = t1.filter(result -> result < 0); // condition is not met

    assertNotEquals(t1, t2); // t1 is still active

    assertFalse(t2.isActive()); // t2 is now passive
    assertTrue(t2.isPassive()); // t2 is now passive
    assertFalse(t2.isResult()); // t2 no longer has a result
    assertTrue(t2.isSuccess()); // t2  is still a success

    t2.map(result -> result + 1); // t2 is still passive
    assertTrue(t2.get() == null);
  }

  @Test
  void test_try_without_result_states() {
    Try<?> t1 =
        Try.of(
            () -> {
              Integer.parseInt("25");
            });

    assertTrue(t1.isActive());

    Try<?> t2 = t1.filter(Objects::isNull); // condition is met

    assertEquals(t1, t2);

    assertTrue(t1.isActive());

    t2 = t1.filter(Objects::nonNull); // condition is not met

    assertTrue(t2.isActive()); // t2 is still active
    assertFalse(t2.isPassive()); // t2 not passive
    assertFalse(t2.isResult()); // t2 still no longer has a result
    assertTrue(t2.isSuccess()); // t1  is still a success
  }
}
