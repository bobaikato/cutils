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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import art.cutils.function.Condition;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test Custom Functions")
final class ConditionTest {

  private static @NotNull Stream<Arguments> trueConditionFunctions() {
    final Condition firstCondition = () -> true;
    final Condition secondCondition = () -> true;

    return Stream.of(Arguments.of(firstCondition), Arguments.of(secondCondition));
  }

  private static @NotNull Stream<Arguments> falseConditionFunctions() {
    final Condition firstCondition = () -> false;
    final Condition secondCondition = () -> false;

    return Stream.of(Arguments.of(firstCondition), Arguments.of(secondCondition));
  }

  @DisplayName("Should return Boolean True based on the given conditions.")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("trueConditionFunctions")
  void verifyReturnExpectedValuesOfSameType(@NotNull Condition condition) {
    assertTrue(condition.isMet());
    assertFalse(condition.isNotMet());
  }

  @DisplayName("Should return Boolean False based on the given conditions.")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("falseConditionFunctions")
  void verifyDealerThrowsException(@NotNull Condition condition) {
    assertFalse(condition.isMet());
    assertTrue(condition.isNotMet());
  }
}