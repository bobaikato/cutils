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
package string;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.honerfor.cutils.string.StringIncubator;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


@DisplayName("Test Random String Hatching.")
final class StringIncubatorTest {

  @DisplayName("Should successfully hatch specify length of random String.")
  @ParameterizedTest(name = "{index} => Length={0}, Random Instance={1}")
  @MethodSource("resourceI")
  void shouldHatchRandomStringOfSpecifyLength(int length, Random random) {
    final String hatchedString = new StringIncubator(length, random).hatch();
    assertNotEquals(hatchedString, null);
    assertEquals(hatchedString.length(), length);
  }

  private static Stream<Arguments> resourceI() {
    return Stream.of(
        Arguments.of(10, new Random()),
        Arguments.of(128, ThreadLocalRandom.current()),
        Arguments.of(30, new SecureRandom()),
        Arguments.of(1000, ThreadLocalRandom.current()));
  }

  @DisplayName("Should successfully hatch unique random string.")
  @ParameterizedTest(name = "{index} => First hatch={0}, Second hatch={1}")
  @MethodSource("resourceII")
  void shouldHatchUniqueStringsOfSpecifyLength(String firstString, String secondString) {
    assertNotEquals(firstString, null);
    assertNotEquals(secondString, null);
    assertNotEquals(firstString, secondString);
  }

  private static Stream<Arguments> resourceII() {
    return Stream.of(
        Arguments.of(new StringIncubator().hatch(), new StringIncubator().hatch()),
        Arguments.of(
            new StringIncubator(12, new SecureRandom(), "Symb0l").hatch(),
            new StringIncubator(12, new SecureRandom(), "Symb0l").hatch()),
        Arguments.of(new StringIncubator(23).hatch(), new StringIncubator(23).hatch()));
  }

  @Test
  @DisplayName("Should successfully hatch String with 64 characters.")
  void defaultHatchStringSizeShouldBe64() {
    final String hatchedString = new StringIncubator().hatch();
    assertEquals(hatchedString.length(), 64);
  }

  @Test
  @DisplayName(
      "Should successfully hatch String from supplied symbol of specified length characters.")
  void defaultHatchStringSizeShouldBeOFSpecifiedSizeAndSymbol() {
    final String hatchedString = new StringIncubator(12, "AaDdZz").hatch();
    assertEquals(hatchedString.length(), 12);
  }

  @DisplayName("Should throw IllegalArgumentException when specified length is less than 1.")
  @ParameterizedTest(name = "{index} => Length={0}")
  @MethodSource("resourceIII")
  void throwIllegalArgumentExceptionOnInvalidLength(int length) {
    assertThrows(IllegalArgumentException.class, () -> new StringIncubator(length).hatch());
  }

  private static Stream<Arguments> resourceIII() {
    return Stream.of(Arguments.of(-10), Arguments.of(0), Arguments.of(-30), Arguments.of(-1));
  }

  @DisplayName("Should throw IllegalArgumentException when specified Symbol is less than 2.")
  @ParameterizedTest(name = "{index} => Symbol={0}, Length={1}")
  @MethodSource("resourceIV")
  void throwIllegalArgumentExceptionOnInvalidSymbol(String symbol) {
    assertThrows(
        IllegalArgumentException.class,
        () -> new StringIncubator(12, new SecureRandom(), symbol).hatch());
  }

  private static Stream<Arguments> resourceIV() {
    return Stream.of(Arguments.of("1"), Arguments.of("a"), Arguments.of("D"));
  }
}
