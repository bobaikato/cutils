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

package art.cutils.collection;

import static java.lang.String.format;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides a means to partition List into List of sublist.
 *
 * @author Szymon Stepniak
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @since 1.0
 */
public final class ListPartition<T> extends AbstractList<List<? super T>> {

  /**
   * Variable to hold List.
   *
   * @since 1.0
   */
  private final List<? extends T> list;

  /**
   * variable to hold the sublist size.
   *
   * @since 1.0
   */
  private int sublistSize;

  /**
   * Sole constructor.
   *
   * @param list List to be partitioned.
   */
  private ListPartition(final List<? extends T> list) {
    this.list = new ArrayList<>(list);
  }

  /**
   * Method to receive the list to be partitioned.
   *
   * @param list List to be partitioned.
   * @param <T> List type
   * @return instance of {@link ListPartition}
   */
  @Contract("_ -> new")
  public static <T> @NotNull ListPartition<T> of(final List<? extends T> list) {
    Objects.requireNonNull(list, "List cannot be null");
    return new ListPartition<>(list);
  }

  /**
   * Method used to get partition size.
   *
   * @param sublistSize The sub-list/ListPartition size.
   * @return current instance of {@link ListPartition}
   */
  @Contract(value = "_ -> this", mutates = "this")
  public ListPartition<T> into(final int sublistSize) {
    Validate.isTrue(sublistSize > 0, "Sub-list size must be greater than 0.");
    this.sublistSize = sublistSize;
    return this;
  }

  @Contract(pure = true)
  @Override
  public @NotNull List<T> get(final int index) {
    final int start = index * this.sublistSize;
    final int end = Math.min(start + this.sublistSize, this.list.size());
    if (start > end) {
      throw new IndexOutOfBoundsException(
          format("Index %d is out of the list range <0,%d>", index, this.size() - 1));
    }
    return new ArrayList<>(this.list.subList(start, end));
  }

  @Override
  public int size() {
    return (int) Math.ceil((double) this.list.size() / (double) this.sublistSize);
  }

  @Override
  @Contract(value = "null -> false", pure = true)
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ListPartition) {
      if (!super.equals(o)) {
        return false;
      }
      final ListPartition<?> listPartition = (ListPartition<?>) o;
      return this.sublistSize == listPartition.sublistSize && this.list.equals(listPartition.list);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.list, this.sublistSize);
  }
}
