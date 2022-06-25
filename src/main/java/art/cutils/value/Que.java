/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2022 Bobai Kato. All Rights Reserved.
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

import art.cutils.function.Accepter;
import art.cutils.function.Dealer;
import art.cutils.function.Executable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This is {@link Que} gotten from the word Cue. This is intended to give you the ability to
 * orchestrate operation while also, signalling precise action(s) and flow with full read(ability).
 *
 * @param <T> type.
 * @author @author <a href="https://github.com/B0BAI">Bobai Kato</a>
 * @since 1.0
 */
public final class Que<T> implements Serializable {
  private static final long serialVersionUID = 2637789154313032412L;
  /**
   * This will hold instance of {@link Que} which will be used to enforce singleton.
   *
   * @since 1.0
   */
  private static Que<?> instance;
  /**
   * This hold this holds the final value an operation.
   *
   * @since 1.0
   */
  private final transient T value;

  /** Constructs an empty instance. */
  @Contract(pure = true)
  private Que() {
    this.value = null;
  }

  /**
   * Constructs an instance with the described value.
   *
   * @param value the value to describe
   */
  @Contract(pure = true)
  private Que(final T value) {
    this.value = value;
  }

  /**
   * This method will set {@code value} and returns instance of {@link Que} for other sequential
   * Operations.
   *
   * @param value {@link Que} value
   * @param <T> value Type
   * @return Returns instance of {@link Que}
   */
  @Contract(value = "_ -> new", pure = true)
  public static <T> @NotNull Que<T> of(final T value) {
    return Que.createReference(value);
  }

  /**
   * This method is a helper method used to create {@link Que} reference of a {@code value}.
   *
   * @param value the {@code value} of the reference
   * @param <T> Type of the {@code value}
   * @return an instance of {@link Que}
   * @since 1.0
   */
  @Contract(value = "_ -> new", pure = true)
  private static <T> @NotNull Que<T> createReference(final T value) {
    return new Que<>(value);
  }

  /**
   * This method will take a {@link Supplier} of Type t and will set {@code value} and returns
   * instance of {@link Que} for other sequential Operations.
   *
   * @param supplier variable of Type value
   * @param <T> Type of value
   * @return instance of {@link Que}
   */
  @Contract("_ -> new")
  public static <T> @NotNull Que<T> of(final @NotNull Supplier<? extends T> supplier) {
    return Que.createReference(supplier.get());
  }

  /**
   * This method will take a {@link Dealer} of Type {@code t} and will set {@code value} and returns
   * instance of {@link Que} for other sequential Operations.
   *
   * @param dealer variable of Type value
   * @param <T> Type of value
   * @return instance of {@link Que}
   */
  public static <T> @NotNull Que<T> as(final Dealer<? extends T> dealer) throws Exception {
    Objects.requireNonNull(dealer, "dealer cannot be null");
    return Que.createReference(dealer.deal());
  }

  /**
   * This method will run a {@link Runnable} instance.
   *
   * @param runnable {@link Runnable} type variable
   * @param <T> Type of value
   * @return returns new instance of {@link Que}
   * @since 1.0
   */
  public static <T> Que<T> run(final Runnable runnable) {
    Objects.requireNonNull(runnable, "runnable cannot be null");
    runnable.run();
    return Que.getInstance();
  }

  /**
   * The method should be used to get {@link Que} instance.
   *
   * @param <T> Type of value
   * @return existing or newly created instance of {@link Que}
   */
  private static <T> Que<T> getInstance() {
    if (Objects.isNull(instance)) {
      synchronized (Que.class) {
        if (Objects.isNull(instance)) {
          instance = new Que<>();
        }
      }
    }
    return (Que<T>) instance;
  }

  /**
   * This method will run a {@link Executable} instance Use when Operation will throw an exception.
   *
   * @param executable {@link Runnable} type variable
   * @param <T> Type of value
   * @return returns new instance of {@link Que}
   * @throws Exception this can be any exception throw when executing
   * @since 1.0
   */
  public static <T> Que<T> execute(final Executable executable) throws Exception {
    Objects.requireNonNull(executable, "executable cannot be null");
    executable.execute();
    return Que.getInstance();
  }

  /**
   * This method will consume execute {@link Consumer} type variable.
   *
   * @param consumer {@link Consumer} type variable.
   * @return existing instance of the {@link Que}
   * @since 1.0
   */
  @Contract("_ -> this")
  public Que<T> run(final Consumer<? super T> consumer) {
    return this.consumer(consumer);
  }

  @Contract("_ -> this")
  private Que<T> consumer(final Consumer<? super T> consumer) {
    Objects.requireNonNull(consumer, "consumer cannot be null");
    consumer.accept(this.value);
    return this;
  }

  /**
   * This method will Accept and execute {@link Accepter} type variable.
   *
   * @param accepter {@link Accepter} type variable.
   * @return existing instance of the {@link Que}
   * @since 1.0
   */
  @Contract("_ -> this")
  public Que<T> execute(final Accepter<? super T> accepter) throws Exception {
    return this.accepter(accepter);
  }

  @Contract("_ -> this")
  private Que<T> accepter(final Accepter<? super T> accepter) throws Exception {
    Objects.requireNonNull(accepter, "accepter cannot be null");
    accepter.accept(this.value);
    return this;
  }

  /**
   * This method will execute a {@link Runnable} type variable.
   *
   * @param runnable {@link Runnable} type variable
   * @return existing instance of {@link Que}
   * @since 1.0
   */
  @Contract("_ -> this")
  public Que<T> andRun(final Runnable runnable) {
    Objects.requireNonNull(runnable, "runnable cannot be null");
    runnable.run();
    return this;
  }

  /**
   * Use when Operation will throw an exception.
   *
   * @param executable {@link Executable} type variable
   * @return existing instance of {@link Que}
   * @throws Exception instance of any exception thrown.
   * @since 1.0
   */
  @Contract("_ -> this")
  public Que<T> andExecute(final Executable executable) throws Exception {
    Objects.requireNonNull(executable, "executable cannot be null");
    executable.execute();
    return this;
  }

  /**
   * This method will supply data {@link Que#value} variable.
   *
   * @param supplier {@link Supplier} variable
   * @return existing instance of {@link Que}
   * @since 1.0
   */
  public @NotNull Que<T> andSupply(final Supplier<? extends T> supplier) {
    Objects.requireNonNull(supplier, "supplier cannot be null");
    return Que.createReference(supplier.get());
  }

  /**
   * This method will deal data {@link Que#value} variable. Use this method in place of {@link
   * Supplier} if operation will throw an {@link Exception}.
   *
   * @param dealer {@link Dealer} variable
   * @return existing instance of {@link Que}
   * @since 1.0
   */
  public @NotNull Que<T> andDeal(final Dealer<? extends T> dealer) throws Exception {
    Objects.requireNonNull(dealer, "dealer cannot be null");
    return Que.createReference(dealer.deal());
  }

  /**
   * This method will consume a {@link Consumer} type variable.
   *
   * @param consumer {@link Consumer} type variable
   * @return existing instance of {@link Que}
   * @since 1.0
   */
  @Contract("_ -> this")
  public Que<T> andConsume(final Consumer<? super T> consumer) {
    return this.consumer(consumer);
  }

  /**
   * This method will accept na {@link Accepter} type variable. Use when operation will/may throw
   * and {@link Exception}
   *
   * @param accepter {@link Consumer} type variable
   * @return existing instance of {@link Que}
   * @since 1.0
   */
  @Contract("_ -> this")
  public Que<T> andAccept(final Accepter<? super T> accepter) throws Exception {
    return this.accepter(accepter);
  }

  /**
   * This method will execute {@link Callable} type variable.
   *
   * @param callable {@link Callable} type variable.
   * @return the result type of method {@code call}
   * @throws Exception instance of any exception thrown.
   * @since 1.0
   */
  public @NotNull Que<T> andCall(final Callable<? extends T> callable) throws Exception {
    Objects.requireNonNull(callable, "callable cannot be null");
    return Que.createReference(callable.call());
  }

  /**
   * Used to get the current set value.
   *
   * @return {@link Que#value}
   * @since 1.0
   */
  @Contract(pure = true)
  public T get() {
    return this.value;
  }

  /**
   * Used to get current value of {@link CompletableFuture} type.
   *
   * @return {@link CompletableFuture} of {@link Que#value}
   * @since 1.0
   */
  public @NotNull CompletableFuture<T> completableFuture() {
    return CompletableFuture.completedFuture(this.value);
  }

  /**
   * Used to get current value of {@link Optional} type.
   *
   * @return {@link Optional} of {@link Que#value}
   * @since 1.0
   */
  @Contract(pure = true)
  public Optional<T> optional() {
    return Optional.ofNullable(this.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }

  @Override
  @Contract(value = "null -> false", pure = true)
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Que) {
      final Que<?> que = (Que<?>) o;
      return this.value.equals(que.value);
    } else {
      return false;
    }
  }
}
