/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2021 Honerfor, Inc. All Rights Reserved.
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.honerfor.cutils.function.ThrowingUnaryOperation;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class ThrowingUnaryOperatorTest {

  private static Stream<Arguments> throwingUnaryOperations() {
    final UnaryOperator<String> someTask =
        ThrowingUnaryOperation.unchecked(
            string -> {
              Thread.sleep(Integer.parseInt(string));
              return string;
            });

    return Stream.of(of(someTask, ""), of(someTask, "12E4"), of(someTask, "O"));
  }

  @DisplayName("Should take checked exception operation and throw the exception when thrown")
  @ParameterizedTest(name = "{index} => input={1}")
  @MethodSource("throwingUnaryOperations")
  void throwingUnaryOperations(final UnaryOperator<String> operator, final String input) {
    assertThrows(Exception.class, () -> operator.apply(input));
  }

  @Test
  void testThrowingUnaryOperationsIdentity() {
    Assertions.assertEquals(3, ThrowingUnaryOperation.identity().apply(3));
  }
}
