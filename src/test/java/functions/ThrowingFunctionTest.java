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

import com.honerfor.cutils.function.ThrowingFunction;
import com.honerfor.cutils.value.Que;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class ThrowingFunctionTest {

  @DisplayName("Should take checked exception operationa and throw the exception when thrown")
  @ParameterizedTest(name = "{index} => input={1}")
  @MethodSource("throwingFunctionOperations")
  void throwingFunctionOperations(final Function<String, Integer> fn, final String input) {
    assertThrows(Exception.class, () -> fn.apply(input));
  }

  private static Stream<Arguments> throwingFunctionOperations() {
    final Function<String, Integer> convertStringToInteger =
        ThrowingFunction.unchecked(
            string -> {
              // checked Exception operation
              return Que.as(() -> Integer.parseInt(string)).get();
            });

    return Stream.of(
        Arguments.of(convertStringToInteger, ""),
        Arguments.of(convertStringToInteger, "12E4"),
        Arguments.of(convertStringToInteger, "O"));
  }
}
