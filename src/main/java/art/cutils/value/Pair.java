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

package art.cutils.value;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A pair of values. Two heads are better than one—sometimes.
 *
 * @param <F> first value type
 * @param <S> second value type
 * @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @since 2.0
 */
public final class Pair<F, S> {

  /** First value. */
  private F first;
  /** Second value. */
  private S second;

  /**
   * Constructor to create a pair of values.
   *
   * @param first the first value. Can be null.
   * @param second the second value. Can be null.
   */
  @Contract(pure = true)
  private Pair(final F first, final S second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Creates a pair of values.
   *
   * @param first the first value. Can be null.
   * @param second the second value. Can be null.
   * @return a pair of values
   */
  @Contract(value = "_, _ -> new", pure = true)
  public static <F, S> @NotNull Pair<F, S> of(final F first, final S second) {
    return new Pair<>(first, second);
  }

  /**
   * Create and empty pair.
   *
   * @return new instance of an empty {@link Pair}
   * @param <F> first value type
   * @param <S> second value type
   */
  @Contract(value = " -> new", pure = true)
  public static <F, S> @NotNull Pair<F, S> empty() {
    return new Pair<>(null, null);
  }

  /**
   * Retrieves the first value stored in the Pair.
   *
   * @return the first value stored in the Pair
   */
  @Contract(pure = true)
  public F getFirst() {
    return this.first;
  }

  /**
   * Sets the first value of the Pair and returns the modified Pair.
   *
   * @param first the new value for the first element of the Pair
   * @return the modified Pair with the updated first value
   */
  @Contract(mutates = "this")
  public Pair<F, S> first(final F first) {
    this.first = first;
    return this;
  }

  /**
   * Sets the first and second values.
   *
   * @param first the first value
   * @param second the second value
   */
  @Contract(mutates = "this")
  public void setFirstAndSecond(final F first, final S second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Remove both values to null from the {@link Pair}.
   *
   * @return current instance of {@link Pair}
   */
  public Pair<F, S> clear() {
    return this.deleteFirst().deleteSecond();
  }

  /**
   * Resets the first value of the pair to null.
   *
   * @return The current instance of {@link Pair}.
   */
  @Contract(mutates = "this")
  public Pair<F, S> deleteFirst() {
    this.first = null;
    return this;
  }

  /**
   * Resets the second value to null.
   *
   * @return The current instance of Pair with the second value set to null.
   */
  @Contract(mutates = "this")
  public Pair<F, S> deleteSecond() {
    this.second = null;
    return this;
  }

  /**
   * Retrieves the second value of the pair.
   *
   * @return the second value of the pair
   */
  @Contract(pure = true)
  public S getSecond() {
    return this.second;
  }

  /**
   * Swaps the first and second values of the Pair and returns a new instance of Pair with the
   * swapped values.
   *
   * @implNote A new instance of Pair will be created with the first and second values swapped.
   * @return A new instance of Pair with the first and second values swapped.
   */
  @Contract(value = " -> new", pure = true)
  public @NotNull Pair<S, F> swap() {
    return Pair.of(this.second, this.first);
  }

  /**
   * Sets the second value of the pair and returns the modified pair.
   *
   * @param second the new value for the second element of the pair. Can be null.
   * @return the modified pair with the updated second value.
   */
  @Contract(mutates = "this")
  public Pair<F, S> second(final S second) {
    this.second = second;
    return this;
  }

  /**
   * Checks if both values are not null.
   *
   * @return {@code true} if both values are not null, {@code false} otherwise
   */
  @Contract(pure = true)
  public boolean isNotEmpty() {
    return !this.isEmpty();
  }

  /**
   * Returns true if both values are null.
   *
   * @return true if both values are null
   */
  @Contract(pure = true)
  public boolean isEmpty() {
    return this.first == null && this.second == null;
  }

  /**
   * Checks if the Pair does not have a first value.
   *
   * @return True if the Pair does not have a first value, false otherwise.
   */
  @Contract(pure = true)
  public boolean notHaveFirst() {
    return !this.hasFirst();
  }

  /**
   * Check if the Pair has the first value.
   *
   * @return true if the Pair has the first value, false otherwise
   */
  @Contract(pure = true)
  public boolean hasFirst() {
    return this.first != null;
  }

  /**
   * Checks whether the Pair object does not have a second value.
   *
   * @return {@code true} if the Pair object has no second value, {@code false} otherwise
   */
  @Contract(pure = true)
  public boolean notHaveSecond() {
    return !this.hasSecond();
  }

  /**
   * Check if the Pair has a second value.
   *
   * @return true if the Pair has a second value, false otherwise
   */
  @Contract(pure = true)
  public boolean hasSecond() {
    return this.second != null;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(this.first).append(this.second).toHashCode();
  }

  @Override
  @Contract(value = "null -> false", pure = true)
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    final Pair<?, ?> pair = (Pair<?, ?>) o;

    return new EqualsBuilder()
        .append(this.first, pair.first)
        .append(this.second, pair.second)
        .isEquals();
  }

  @Override
  @Contract(pure = true)
  public @NotNull String toString() {
    return "Pair{" + "first=" + this.first + ", second=" + this.second + '}';
  }
}
