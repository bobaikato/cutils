/*
 * Copyright (C) 2018 — 2020 Honerfor, Inc. All Rights Reserved.
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

package com.honerfor.cutils.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * The {@link Idler}r is intended to be <b>memorized and idempotent</b>. This can be very useful for
 * operations with high latency whose result are used multitudinous. The operation is performed once
 * and the result and served whenever needed and hence improving the performance.
 *
 * @param <T> the type value
 * @author B0BAI
 * @see Supplier
 * @see Dealer
 * @since 5.0
 */
public class Idler<T> implements Supplier<T>, Dealer<T>, Serializable {
  private static final long serialVersionUID = -909341387550414732L;

  private transient Supplier<T> supplier = null;

  private transient Dealer<T> dealer = null;

  private transient T value = null;

  /**
   * Sealed constructor takes the supplier.
   *
   * @param supplier instance of {@link Supplier}
   */
  private Idler(final Supplier<T> supplier) {
    this.supplier = supplier;
  }

  /**
   * Sealed constructor takes the dealer.
   *
   * @param dealer instance of {@link Dealer}
   */
  private Idler(final Dealer<T> dealer) {
    this.dealer = dealer;
  }

  /**
   * Supply take an instance of {@link Supplier} as parameter..
   *
   * @param <T> the type parameter
   * @param supplier the supplier, an instance of {@link Supplier}
   * @return the supplier, an instance of {@link Supplier}
   */
  public static <T> Supplier<T> supply(final Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "supplier cannot be null");
    return new Idler<>(supplier);
  }

  /**
   * Gets a result.
   *
   * @return a result
   */
  @Override
  public T get() {
    if (Objects.nonNull(this.supplier) && Objects.isNull(this.value)) {
      this.value = this.supplier.get();
    }
    return this.value;
  }

  /**
   * Supply take an instance of {@link Dealer} as parameter..
   *
   * @param <T> the type parameter
   * @param dealer the supplier, an instance of {@link Dealer}
   * @return the supplier, an instance of {@link Dealer}
   */
  public static <T> Dealer<T> deal(final Dealer<T> dealer) {
    Objects.requireNonNull(dealer, "dealer cannot be null");
    return new Idler<>(dealer);
  }

  /**
   * Gets a result.
   *
   * @return a result
   */
  @Override
  public T deal() throws Exception {
    if (Objects.nonNull(this.dealer) && Objects.isNull(this.value)) {
      this.value = this.dealer.deal();
    }
    return this.value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Idler) {
      final Idler<?> idler = (Idler<?>) o;
      if (supplier.equals(idler.supplier) && dealer.equals(idler.dealer)) {
        return value.equals(idler.value);
      }
      return false;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(supplier, dealer, value);
  }
}
