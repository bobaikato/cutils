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

package art.cutils.function;

import java.util.Arrays;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an operation that returns a condition/boolean. whose functional method is {@link
 * #isMet()}.
 *
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @since 1.0
 */
@FunctionalInterface
public interface Condition {

  /**
   * Returns a composed condition that represents a short-circuiting logical NOT of this condition
   *
   * @param conditions a condition that will be logically-NOTed with this condition
   * @return a composed condition that represents the short-circuiting logical NOT of this condition
   */
  @Contract(pure = true)
  static @NotNull Condition areAllMet(final Condition... conditions) {
    return () -> Arrays.stream(conditions).noneMatch(Condition::isNotMet);
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical NOT of this condition
   *
   * @param conditions a condition that will be logically-NOTed with this condition
   * @return a composed condition that represents the short-circuiting logical NOT of this condition
   */
  @Contract(pure = true)
  static @NotNull Condition anyMet(final Condition @NotNull ... conditions) {
    return () -> Arrays.stream(conditions).anyMatch(Condition::isMet);
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical NOT of this condition
   *
   * @param conditions a condition that will be logically-NOTed with this condition
   * @return a composed condition that represents the short-circuiting logical NOT of this condition
   */
  @Contract(pure = true)
  static @NotNull Condition noneMet(final Condition @NotNull ... conditions) {
    return () -> Arrays.stream(conditions).noneMatch(Condition::isMet);
  }

  /**
   * Returns a {@link Condition} of the boolean value.
   *
   * @param value the boolean value
   * @return a {@link Condition} of the boolean value
   */
  @Contract(pure = true)
  static @NotNull Condition of(final boolean value) {
    return () -> value;
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical NOT of this condition
   *
   * @param conditions a condition that will be logically-NOTed with this condition
   * @return a composed condition that represents the short-circuiting logical NOT of this condition
   */
  @Contract(pure = true)
  static @NotNull Condition allMet(final Condition @NotNull ... conditions) {
    return () -> Arrays.stream(conditions).anyMatch(Condition::isNotMet);
  }

  /**
   * Negates the condition. True is condition isn't met and False when condition is met.
   *
   * @return the negated condition.
   * @since 1.0
   */
  default boolean isNotMet() {
    return !this.isMet();
  }

  /**
   * Check the condition.
   *
   * @return the final condition of boolean
   * @since 1.0
   */
  boolean isMet();

  /**
   * Returns a composed condition that represents a short-circuiting logical AND of this condition
   *
   * @param other a condition that will be logically-ANDed with this condition
   * @return a composed condition that represents the short-circuiting logical AND of this condition
   */
  default Condition and(final Condition other) {
    return () -> this.isMet() && other.isMet();
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical OR of this condition
   *
   * @param other a condition that will be logically-ORed with this condition
   * @return a composed condition that represents the short-circuiting logical OR of this condition
   */
  default Condition or(final Condition other) {
    return () -> this.isMet() || other.isMet();
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical XOR of this condition
   *
   * @implNote XOR is exclusive.
   * @param other a condition that will be logically-XORed with this condition
   * @return a composed condition that represents the short-circuiting logical XOR of this condition
   */
  default Condition xor(final Condition other) {
    return () -> this.isMet() ^ other.isMet();
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical NAND of this condition
   *
   * @implNote NAND is the negation of AND (Not AND)
   * @param other
   * @return a composed condition that represents the short-circuiting logical NAND of this
   *     condition
   */
  default Condition nand(final Condition other) {
    return () -> !(this.isMet() && other.isMet());
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical NOR of this condition
   *
   * @param other a condition that will be logically-NORed with this condition
   * @return a composed condition that represents the short-circuiting logical NOR of this condition
   */
  default Condition nor(final Condition other) {
    return () -> !(this.isMet() || other.isMet());
  }

  /**
   * Returns a composed condition that represents a short-circuiting logical XNOR of this condition
   *
   * @param other a condition that will be logically-XNORed with this condition
   * @return a composed condition that represents the short-circuiting logical XNOR of this
   *     condition
   */
  default Condition xnor(final Condition other) {
    return () -> this.isMet() == other.isMet();
  }
}
