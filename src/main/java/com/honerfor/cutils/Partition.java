/*
 * Copyright (C) 2018 — 2019 Honerfor, Inc. All Rights Reserved.
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

package com.honerfor.cutils;

import static java.lang.String.format;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.Validate;

/**
 * This class provide a means to partition List into List of sublist.
 *
 * @author Szymon Stepniak
 * @author B0BAI
 * @since v1.0
 */
public class Partition<T> extends AbstractList<List<T>> {

  /**
   * Variable to hold List.
   *
   * @since v1.0
   */
  private final List<T> list;

  /**
   * variable to hold the sublist size.
   *
   * @since v1.0
   */
  private int sublistSize;

  /**
   * Sole constructor.
   *
   * @param list List to be partitioned.
   */
  private Partition(List<T> list) {
    this.list = new ArrayList<>(list);
  }

  /**
   * Method to receive the list to be partitioned.
   *
   * @param list List to be partitioned.
   * @param <T> List type
   * @return instance of {@link Partition}
   */
  public static <T> Partition<T> of(List<T> list) {
    Objects.requireNonNull(list, "List cannot be null");
    return new Partition<>(list);
  }

  /**
   * Method used to get partition size.
   *
   * @param sublistSize The sub-list/Partition size.
   * @return current instance of {@link Partition}
   */
  public Partition<T> into(int sublistSize) {
    Validate.isTrue(sublistSize > 0, "Sub-list size must be greater than 0.");
    this.sublistSize = sublistSize;
    return this;
  }

  @Override
  public List<T> get(int index) {
    final int start = index * sublistSize;
    final int end = Math.min(start + sublistSize, list.size());
    if (start > end) {
      throw new IndexOutOfBoundsException(
        format("Index %d is out of the list range <0,%d>", index, this.size() - 1));
    }
    return new ArrayList<>(list.subList(start, end));
  }

  @Override
  public int size() {
    return (int) Math.ceil((double) list.size() / (double) sublistSize);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Partition)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    final Partition<?> partition = (Partition<?>) o;
    return sublistSize == partition.sublistSize && list.equals(partition.list);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), list, sublistSize);
  }
}
