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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import prohor.dev.cutils.function.Accepter;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class AccepterTest {

  private static Stream<Arguments> accepterFunctions() {

    final Accepter<Class<?>> firstAccepter =
        value -> {
          throw new ClassCastException();
        };

    final Accepter<File> secondAccepter =
        value -> {
          throw new IllegalAccessException();
        };

    final Accepter<String> thirdAccepter =
        value -> {
          throw new NullPointerException();
        };

    return Stream.of(
        Arguments.of(firstAccepter), Arguments.of(secondAccepter), Arguments.of(thirdAccepter));
  }

  @DisplayName("Expect that Accepter Should throw exception")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("accepterFunctions")
  void verifyAccepterThrowsException(Accepter<?> accepter) {
    assertThrows(Exception.class, () -> accepter.andThen(System.out::println).accept(null));
  }

  @Test
  void verifyAccepterOperation() throws Exception {
    final AtomicBoolean flag = new AtomicBoolean(false);

    final Accepter<Boolean> accepter = newValue -> flag.set(true);
    final Accepter<Boolean> acpt = accepter.andThen(flag::set);

    assertFalse(flag.get());

    acpt.accept(false);

    assertFalse(flag.get());
  }
}
