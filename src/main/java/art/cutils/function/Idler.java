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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import art.cutils.value.Pair;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link Idler}r is intended to be <b>memorized and idempotent</b>. This can be very useful for
 * operations with high latency whose result are used multitudinous. The operation is performed once
 * and the result and served whenever needed and hence improving the performance.
 *
 * @param <T> the type value
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @see Supplier
 * @see Dealer
 * @since 1.0
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
  private transient Pair<T, T> pair = Pair.empty();

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

  private Idler(final Dealer<T> dealer, final Supplier<T> supplier) {
    this.dealer = dealer;
    this.supplier = supplier;
  }

  @Contract("_, _ -> new")
  public static <T> @NotNull Idler<T> of(final Dealer<T> dealer, final Supplier<T> supplier) {
    return Idler.of(supplier, dealer);
  }

  @Contract("_, _ -> new")
  public static <T> @NotNull Idler<T> of(final Dealer<T> dealer) {
    return new Idler<>(dealer);
  }

  @Contract("_, _ -> new")
  public static <T> @NotNull Idler<T> of(final Supplier<T> supplier, final Dealer<T> dealer) {
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
  @Contract("_ -> new")
  public static <T> @NotNull Supplier<T> supply(final Supplier<T> supplier) {
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
  @Contract("_ -> new")
  public static <T> @NotNull Dealer<T> deal(final Dealer<T> dealer) {
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
    if (nonNull(this.dealer) && isNull(this.pair.getSecond())) {
      this.pair = this.pair.second(this.dealer.deal());
    }
    return this.pair.getSecond();
  }

  /**
   * Gets a result for supplier operation.
   *
   * @return a supplier result
   * @see Supplier#get
   */
  @Override
  public T get() {
    if (nonNull(this.supplier) && isNull(this.pair.getFirst())) {
      this.pair = this.pair.first(this.supplier.get());
    }
    return this.pair.getFirst();
  }

  @Override
  public int hashCode() {
    int result = this.supplier != null ? this.supplier.hashCode() : 0;
    result = 31 * result + (this.dealer != null ? this.dealer.hashCode() : 0);
    result = 31 * result + this.pair.hashCode();
    return result;
  }

  @Override
  @Contract(value = "null -> false", pure = true)
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Idler) {
      final Idler<?> idler = (Idler<?>) o;

      if (Objects.equals(this.supplier, idler.supplier)) {
        if (!Objects.equals(this.dealer, idler.dealer)) {
          return false;
        }
        return this.pair.equals(idler.pair);
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}
