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

package functions;

import pro.horde.os.cutils.function.Executable;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class ExecutableTest {

  @DisplayName("Expect Dealer throws an Exception.")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("executableFunctions")
  void verifyExecutableThrowsAnException(Executable executable) {
    Assertions.assertThrows(Exception.class, executable::execute);
  }

  private static Stream<Arguments> executableFunctions() {
    final Executable firstExec =
        () -> {
          throw new ClassCastException();
        };

    final Executable secondExec =
        () -> {
          throw new IllegalAccessException();
        };

    final Executable thirdExec =
        () -> {
          throw new NullPointerException();
        };

    return Stream.of(Arguments.of(firstExec), Arguments.of(secondExec), Arguments.of(thirdExec));
  }
}
