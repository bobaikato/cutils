/*
 *  _________  ____ ______________.___.____       _________
 *  \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 *  /    \  \/|    |   / |    |   |   |    |     \_____  \
 *  \     \___|    |  /  |    |   |   |    |___  /        \
 *   \______  /______/   |____|   |___|_______ \/_______  /
 *          \/                                \/        \/
 *
 *  Copyright (C) 2018 — 2021 Bobai Kato. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package art.cutils.string;

import static org.apache.commons.lang3.Validate.isTrue;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * You can use this class if you need to Generate Random String/Alpha-numeric string for Tickets,
 * Session, ID etc. To generate random string you can use the default the constructor that suit your
 * purpose or the default option which will generate string of 64 characters.
 *
 * <p>Example/Usage: <code>
 * </p>
 * StringIncubator gen = new StringIncubator(12);
 * StringIncubator session = new StringIncubator();
 * <p>
 * StringIncubator gen = new StringIncubator(12, ThreadLocalRandom.current());
 * </p>
 * String symbol = StringIncubator.DIGITS + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
 * StringIncubator tickets = new StringIncubator(23, new SecureRandom(), symbol);
 * </code>
 *
 * @author Bobai Kato — https://github.com/B0BAI>
 * @author Erickson (https://stackoverflow.com/users/3474/erickson)
 * @since 2.0
 */
public class StringIncubator implements Serializable {

  private static final long serialVersionUID = 3793946601043438484L;

  private final Random random;

  private final char[] symbols;

  private final char[] buffer;

  /**
   * Upper-case Alphabets.
   *
   * @since 2.0
   */
  private static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * Lower-case Alphabets.
   *
   * @since 2.0
   */
  private static String lower = getUpper().toLowerCase(Locale.ROOT);

  /**
   * Digits variable.
   *
   * @since 2.0
   */
  public static final String DIGITS = "0123456789";

  /**
   * Alpha-numeric variable.
   *
   * @since 2.0
   */
  private static final String ALPHANUM = getUpper() + getLower() + DIGITS;

  /**
   * Constructor which take the require hatch length, instance of {@link Random} and {@link String}
   * from which to hatch the random values.
   *
   * @param length of hatched string/value
   * @param random any instance of {@link Random} as required
   * @param symbols String to generate the hatched values
   * @since 2.0
   */
  public StringIncubator(final int length, final Random random, final String symbols) {
    this.symbols = Objects.requireNonNull(symbols, "symbols cannot be null").toCharArray();

    isTrue(length > 1, "String length cannot be less than 1", length);
    isTrue(symbols.length() > 2, "Symbols length cannot be less that 2", symbols.length());

    this.random = Objects.requireNonNull(random);
    this.buffer = new char[length];
  }

  /**
   * Constructor to hatch an alphanumeric string generator.
   *
   * @param length of hatched string/value
   * @param random any instance of {@link Random} as required
   * @since 2.0
   */
  public StringIncubator(final int length, final Random random) {
    this(length, random, ALPHANUM);
  }

  /**
   * Constructor to hatch an alphanumeric string generator.
   *
   * @param length of hatched string/value
   * @param random any instance of {@link ThreadLocalRandom} as required
   * @since 2.0
   */
  public StringIncubator(final int length, final ThreadLocalRandom random) {
    this(length, random, ALPHANUM);
  }

  /**
   * Create an alphanumeric string from a secure generator.
   *
   * @param length of hatched string/value
   * @since 2.0
   */
  public StringIncubator(final int length) {
    this(length, new SecureRandom());
  }

  /**
   * Create an alphanumeric string from a secure generator.
   *
   * @param symbols String to generate the hatched values
   * @param length of hatched string/value
   * @since 3.0
   */
  public StringIncubator(final int length, final String symbols) {
    this(length, new SecureRandom(), symbols);
  }

  /**
   * Create session identifiers, default length of 64.
   *
   * @since 2.0
   */
  public StringIncubator() {
    this(64);
  }

  /**
   * Upper-case Alphabets.
   *
   * @since 2.0
   */
  public static String getUpper() {
    return upper;
  }

  /**
   * Use this set the Upper case characters.
   *
   * @param uppercaseValue the upper case values.
   */
  public static void setUpper(String uppercaseValue) {
    StringIncubator.upper = uppercaseValue.toUpperCase();
  }

  /**
   * Lower-case Alphabets.
   *
   * @since 2.0
   */
  public static String getLower() {
    return lower;
  }

  /**
   * Use this method to set Lower-case characters.
   *
   * @param lowercaseValue the lower case values.
   */
  public static void setLower(final String lowercaseValue) {
    StringIncubator.lower = lowercaseValue.toLowerCase();
  }

  /**
   * Generate and returns random string.
   *
   * @since 2.0
   */
  public String hatch() {
    IntStream.range(0, buffer.length)
        .forEachOrdered(idx -> buffer[idx] = symbols[random.nextInt(symbols.length)]);
    return String.valueOf(buffer);
  }
}
