/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;
import org.javatuples.Pair;

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
public final class Idler<T> implements Supplier<T>, Dealer<T>, Serializable {
  private static final long serialVersionUID = -909341387550414732L;

  private transient Supplier<T> supplier;

  private transient Dealer<T> dealer;

  /**
   * Object to hold information.
   *
   * <p>0: Supplier
   *
   * <p>1: Dealer
   */
  private transient Pair<T, T> pair = Pair.with(null, null);

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
   * Sealed constructor.
   *
   * @param dealer instance of {@link Dealer}
   * @param supplier instance of {@link Supplier}
   */
  private Idler(final Dealer<T> dealer, Supplier<T> supplier) {
    this.dealer = dealer;
    this.supplier = supplier;
  }

  /**
   * use this method to initialize two idle operation of {@link Supplier} and {@link Dealer} types.
   *
   * @param supplier instance of {@link Supplier}
   * @param dealer instance of {@link Dealer}
   * @param <T> instance type
   * @return an {@link Idler} instance.
   */
  public static <T> Idler<T> of(final Supplier<T> supplier, final Dealer<T> dealer) {
    requireNonNull(dealer, "dealer cannot be null");
    requireNonNull(supplier, "supplier cannot be null");
    return new Idler<>(dealer, supplier);
  }

  /**
   * Supply take an instance of {@link Supplier} as parameter..
   *
   * @param <T> the type parameter
   * @param supplier the supplier, an instance of {@link Supplier}
   * @return the supplier, an instance of {@link Supplier}
   */
  public static <T> Supplier<T> supply(final Supplier<T> supplier) {
    requireNonNull(supplier, "supplier cannot be null");
    return new Idler<>(supplier);
  }

  /**
   * Supply take an instance of {@link Dealer} as parameter..
   *
   * @param <T> the type parameter
   * @param dealer the supplier, an instance of {@link Dealer}
   * @return the supplier, an instance of {@link Dealer}
   */
  public static <T> Dealer<T> deal(final Dealer<T> dealer) {
    requireNonNull(dealer, "dealer cannot be null");
    return new Idler<>(dealer);
  }

  /**
   * Gets a result for dealer operation.
   *
   * @return a dealer result
   * @see Dealer#deal
   */
  @Override
  public T deal() throws Exception {
    if (nonNull(this.dealer) && isNull(this.pair.getValue1())) {
      this.pair = this.pair.setAt1(this.dealer.deal());
    }
    return this.pair.getValue1();
  }

  /**
   * Gets a result for supplier operation.
   *
   * @return a supplier result
   * @see Supplier#get
   */
  @Override
  public T get() {
    if (nonNull(this.supplier) && isNull(this.pair.getValue0())) {
      this.pair = this.pair.setAt0(this.supplier.get());
    }
    return this.pair.getValue0();
  }

  @Override
  public int hashCode() {
    int result = supplier != null ? supplier.hashCode() : 0;
    result = 31 * result + (dealer != null ? dealer.hashCode() : 0);
    result = 31 * result + pair.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Idler) {
      final Idler<?> idler = (Idler<?>) o;

      if (!Objects.equals(supplier, idler.supplier)) {
        return false;
      }
      if (!Objects.equals(dealer, idler.dealer)) {
        return false;
      }
      return pair.equals(idler.pair);
    } else {
      return false;
    }
  }
}
