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

import com.honerfor.cutils.function.Accepter;
import com.honerfor.cutils.function.Dealer;
import com.honerfor.cutils.function.Executable;
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
 * @author B0BAI
 * @since 1.0
 */
public final class Que<T> {

  /**
   * This will hold instance of {@link Que} which will be used to enforce singleton.
   *
   * @since 1.0
   */
  private static Que instance;
  /**
   * This hold this holds the final value an operation.
   *
   * @since 1.0
   */
  private final T value;

  /** Constructs an empty instance. */
  private Que() {
    this.value = null;
  }

  /**
   * Constructs an instance with the described value.
   *
   * @param value the value to describe
   */
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
  public static <T> Que<T> of(final T value) {
    return Que.createReference(value);
  }

  /**
   * This method will take a {@link Supplier} of Type t and will set {@code value} and returns
   * instance of {@link Que} for other sequential Operations.
   *
   * @param supplier variable of Type value
   * @param <T> Type of value
   * @return instance of {@link Que}
   */
  public static <T> Que<T> of(final Supplier<T> supplier) {
    return Que.createReference(supplier.get());
  }


  /**
   * This method is a helper method used to create {@link Que} reference of a {@code value}.
   *
   * @param value the {@code value} of the reference
   * @param <T> Type of the {@code value}
   * @return an instance of {@link Que}
   * @since 3.0
   */
  private static <T> Que<T> createReference(final T value) {
    return new Que<>(value);
  }

  /**
   * This method will take a {@link Dealer} of Type {@code t} and will set {@code value} and returns
   * instance of {@link Que} for other sequential Operations.
   *
   * @param dealer variable of Type value
   * @param <T> Type of value
   * @return instance of {@link Que}
   */
  public static <T> Que<T> as(final Dealer<T> dealer) throws Exception {
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
    runnable.run();
    return Que.getInstance();
  }

  /**
   * This method will consume execute {@link Consumer} type variable.
   *
   * @param consumer {@link Consumer} type variable.
   * @return existing instance of the {@link Que}
   * @since 1.0
   */
  public Que<T> run(final Consumer<T> consumer) {
    consumer.accept(this.value);
    return this;
  }

  /**
   * The method should be used to get {@link Que} instance.
   *
   * @param <T> Type of value
   * @return existing or newly created instance of {@link Que}
   */
  @SuppressWarnings("unchecked")
  private static <T> Que<T> getInstance() {
    if (Objects.isNull(instance)) {
      synchronized (Que.class) {
        if (Objects.isNull(instance)) {
          instance = new Que<T>();
        }
      }
    }
    return instance;
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
    executable.execute();
    return Que.getInstance();
  }

  /**
   * This method will Accept and execute {@link Accepter} type variable.
   *
   * @param accepter {@link Accepter} type variable.
   * @return existing instance of the {@link Que}
   * @since 1.0
   */
  public Que<T> execute(final Accepter<T> accepter) throws Exception {
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
  public Que<T> andRun(final Runnable runnable) {
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
  public Que<T> andExecute(final Executable executable) throws Exception {
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
  public Que<T> andSupply(Supplier<T> supplier) {
    return Que.createReference(supplier.get());
  }

  /**
   * This method will deal data {@link Que#value} variable. Use this method in place of {@link
   * Supplier} if operation will throw an {@link Exception}.
   *
   * @param dealer {@link Dealer} variable
   * @return existing instance of {@link Que}
   * @since 2.0
   */
  public Que<T> andDeal(final Dealer<T> dealer) throws Exception {
    return Que.createReference(dealer.deal());
  }

  /**
   * This method will consume a {@link Consumer} type variable.
   *
   * @param consumer {@link Consumer} type variable
   * @return existing instance of {@link Que}
   * @since 1.0
   */
  public Que<T> andConsume(final Consumer<T> consumer) {
    consumer.accept(this.value);
    return this;
  }

  /**
   * This method will accept na {@link Accepter} type variable. Use when operation will/may throw
   * and {@link Exception}
   *
   * @param accepter {@link Consumer} type variable
   * @return existing instance of {@link Que}
   * @since 1.0
   */
  public Que<T> andAccept(final Accepter<T> accepter) throws Exception {
    accepter.accept(this.value);
    return this;
  }

  /**
   * This method will execute {@link Callable} type variable.
   *
   * @param callable {@link Callable} type variable.
   * @return the result type of method {@code call}
   * @throws Exception instance of any exception thrown.
   * @since 1.0
   */
  public Que<T> andCall(final Callable<T> callable) throws Exception {
    return Que.createReference(callable.call());
  }

  /**
   * Used to get the current set value.
   *
   * @return {@link Que#value}
   * @since 1.0
   */
  public T get() {
    return value;
  }

  /**
   * Used to get current value of {@link CompletableFuture} type.
   *
   * @return {@link CompletableFuture} of {@link Que#value}
   * @since 1.0
   */
  public CompletableFuture<T> completableFuture() {
    return CompletableFuture.completedFuture(this.value);
  }

  /**
   * Used to get current value of {@link Optional} type.
   *
   * @return {@link Optional} of {@link Que#value}
   * @since 3.0
   */
  public Optional<T> optional() {
    return Optional.ofNullable(this.value);
  }
}
