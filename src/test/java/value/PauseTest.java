/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2021 Prohorde, LTD. All Rights Reserved.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.of;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import prohor.dev.cutils.function.Dealer;
import prohor.dev.cutils.function.Executable;
import prohor.dev.cutils.value.Pause;
import prohor.dev.cutils.value.Try;

@DisplayName("PauseTest Operation test.")
final class PauseTest {

  @DisplayName("Should successfully pause operation with no return value.")
  @ParameterizedTest(name = "Time Unit={0}")
  @MethodSource("resource1")
  void shouldPauseOperationWithNoReturnTYpe(final TimeUnit timeUnit, final Executable ex) {
    final Pause<?> pause = Pause.until(1);

    if (timeUnit.equals(TimeUnit.NANOSECONDS)) {
      pause
          .nanoSeconds()
          .thenRun(ex)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertFalse(tryResult.isResult());
              });
    }

    if (timeUnit.equals(TimeUnit.MILLISECONDS)) {
      pause
          .milliSeconds()
          .thenRun(ex)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertFalse(tryResult.isResult());
              });
    }

    if (timeUnit.equals(TimeUnit.SECONDS)) {
      pause
          .seconds()
          .thenRun(ex)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertFalse(tryResult.isResult());
              });
    }

    if (timeUnit.equals(TimeUnit.MICROSECONDS)) {
      pause
          .microSeconds()
          .thenRun(ex)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertFalse(tryResult.isResult());
              });
    }
  }

  private static Stream<Arguments> resource1() {
    final Executable ex = () -> System.out.println("Executable executed after a pause.");
    return Stream.of(
        of(TimeUnit.NANOSECONDS, ex),
        of(TimeUnit.SECONDS, ex),
        of(TimeUnit.MILLISECONDS, ex),
        of(TimeUnit.MICROSECONDS, ex));
  }

  @DisplayName("Should successfully pause operation with return value.")
  @ParameterizedTest(name = "Time Unit={0}")
  @MethodSource("resource2")
  void shouldPauseOperationWithReturnTYpe(final TimeUnit timeUnit, final Dealer<String> dealer) {

    final String returnValue = "Dealer executed after a pause.";
    final Pause<String> pause = Pause.until(1);

    if (timeUnit.equals(TimeUnit.NANOSECONDS)) {
      pause
          .nanoSeconds()
          .thenRun(dealer)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertEquals(returnValue, ((Try<?>) tryResult).get());
              });
    }

    if (timeUnit.equals(TimeUnit.MILLISECONDS)) {
      pause
          .milliSeconds()
          .thenRun(dealer)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertEquals(returnValue, ((Try<?>) tryResult).get());
              });
    }

    if (timeUnit.equals(TimeUnit.SECONDS)) {
      pause
          .seconds()
          .thenRun(dealer)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertEquals(returnValue, ((Try<?>) tryResult).get());
              });
    }

    if (timeUnit.equals(TimeUnit.MICROSECONDS)) {
      pause
          .microSeconds()
          .thenRun(dealer)
          .onComplete(
              tryResult -> {
                assertNotNull(tryResult);
                assertTrue(tryResult instanceof Try);

                assertEquals(returnValue, ((Try<?>) tryResult).get());
              });
    }
  }

  private static Stream<Arguments> resource2() {
    final Dealer<String> dealer = () -> "Dealer executed after a pause.";
    return Stream.of(
        of(TimeUnit.NANOSECONDS, dealer),
        of(TimeUnit.SECONDS, dealer),
        of(TimeUnit.MILLISECONDS, dealer),
        of(TimeUnit.MICROSECONDS, dealer));
  }

  @Test
  void testPauseContracts() {
    final Pause<Object> p1 = Pause.until(1);
    final Pause<Object> p2 = Pause.until(2);

    assertNotEquals(p1, p2);
    assertNotEquals(p1.hashCode(), p2.hashCode());

    assertNotEquals(p1.microSeconds(), p2.microSeconds());
    assertNotEquals(p1.microSeconds().hashCode(), p2.microSeconds().hashCode());

    assertEquals(p1.seconds().thenRun(() -> {}), p2.seconds().thenRun(() -> {}));

    assertNotEquals(
        p1.seconds()
            .thenRun(
                () -> {
                  return "";
                }),
        p2.seconds().thenRun(() -> {}));
  }
}
