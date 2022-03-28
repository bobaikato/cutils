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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.stream.Stream;
import art.cutils.function.Dealer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class DealerTest {

  private static @NotNull Stream<Arguments> firstDealerFunctions() {
    final Dealer<String> firstDealer = () -> "flash";
    final Dealer<Integer> secondDealer = () -> 122;

    return Stream.of(Arguments.of(firstDealer), Arguments.of(secondDealer));
  }

  private static @NotNull Stream<Arguments> secondDealerFunctions() {
    final Dealer<Class<?>> firstDealer =
        () -> {
          throw new ClassCastException();
        };

    final Dealer<File> secondDealer =
        () -> {
          throw new IllegalAccessException();
        };

    final Dealer<String> thirdDealer =
        () -> {
          throw new NullPointerException();
        };

    return Stream.of(
        Arguments.of(firstDealer), Arguments.of(secondDealer), Arguments.of(thirdDealer));
  }

  @DisplayName("Expect Dealer to return variable of same Type that was Initialized.")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("firstDealerFunctions")
  void verifyDealerReturnsExpectedVariableOfSameType(@NotNull Dealer<?> dealer) throws Exception {

    assertNotNull(dealer.deal());

    if (dealer.deal() instanceof Integer) {
      assertEquals(122, dealer.deal());
    }

    if (dealer.deal() instanceof String) {
      assertEquals("flash", dealer.deal());
    }
  }

  @DisplayName("Expect Dealer throws an Exception.")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("secondDealerFunctions")
  void verifyDealerThrowsAnException(@NotNull Dealer<?> dealer) {
    Assertions.assertThrows(Exception.class, dealer::deal);
  }
}