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

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

/**
 * <P>
 * This is {@link Que} gotten from the word Cue. This is intended to give you the ability to
 * orchestrate operation while also, signalling precise action(s) and flow with full read(ability).
 * </p>
 *
 * @param <T> type.
 * @author B0BAI
 * @since 1.0
 */
public class Que<T> {

    /**
     * <p>This hold this holds the final value an operation.</p>
     *
     * @since 1.0
     */
    private T value;

    /**
     * <p>This will hold instance of {@link Que} which will be used to enforce singleton.</p>
     *
     * @since 1.0
     */
    private static WeakReference<Que> instance;

    /**
     * <p>The method should be used to get {@link Que} instance.</p>
     *
     * @param <T> Type of value
     * @return existing or newly created instance of {@link Que}
     */
    @SuppressWarnings("unchecked")
    private static <T> Que<T> getInstance() {
        if (isNull(instance)) {
            synchronized (Que.class) {
                if (isNull(instance)) instance = new WeakReference<>(new Que<T>());
            }
        }
        return instance.get();
    }

    /**
     * @param value the {@code value} of the reference
     * @param <T>   Type of the {@code value}
     * @return an instance of {@link Que}
     * @implNote This method is a helper method used to create {@link Que} reference of a {@code value}
     * @since 3.0
     */
    private static <T> Que<T> createReference(T value) {
        return new WeakReference<>(new Que<>(value))
                .get();
    }

    /**
     * <p>Constructs an empty instance.</p>
     */
    private Que() {
        this.value = null;
    }

    /**
     * <p>Constructs an instance with the described value.</p>
     *
     * @param value the value to describe
     */
    private Que(T value) {
        this.value = value;
    }

    /**
     * <p>
     * This method will set {@code value} and returns instance of
     * {@link Que} for other sequential Operations
     * </p>
     *
     * @param value {@link Que} value
     * @param <T>   value Type
     * @return Returns instance of {@link Que}
     */
    public static <T> Que<T> of(T value) {
        return Que.createReference(value);
    }

    /**
     * <p>
     * This method will take a {@link Supplier} of Type t and will set {@code value} and returns instance of
     * {@link Que} for other sequential Operations
     * </p>
     *
     * @param supplier variable of Type value
     * @param <T>      Type of value
     * @return instance of {@link Que}
     */
    public static <T> Que<T> of(Supplier<T> supplier) {
        return Que.createReference(supplier.get());
    }

    /**
     * <p>
     * This method will take a {@link Dealer} of Type {@code t} and will set {@code value} and returns instance of
     * {@link Que} for other sequential Operations
     * </p>
     *
     * @param dealer variable of Type value
     * @param <T>    Type of value
     * @return instance of {@link Que}
     */
    public static <T> Que<T> as(Dealer<T> dealer) throws Exception {
        return Que.createReference(dealer.deal());
    }

    /**
     * <p> This method will consume execute {@link Consumer} type variable.</p>
     *
     * @param consumer {@link Consumer} type variable.
     * @return existing instance of the {@link Que}
     * @since 1.0
     */
    public Que<T> run(Consumer<T> consumer) {
        consumer.accept(this.value);
        return this;
    }

    /**
     * <p> This method will Accept and execute {@link Accepter} type variable.</p>
     *
     * @param accepter {@link Accepter} type variable.
     * @return existing instance of the {@link Que}
     * @since 1.0
     */
    public Que<T> execute(Accepter<T> accepter) throws Exception {
        accepter.accept(this.value);
        return this;
    }

    /**
     * <p>This method will run a {@link Runnable} instance.</p>
     *
     * @param runnable {@link Runnable} type variable
     * @param <T>      Type of value
     * @return returns new instance of {@link Que}
     * @since 1.0
     */
    public static <T> Que<T> run(Runnable runnable) {
        runnable.run();
        return Que.getInstance();
    }

    /**
     * <p>
     * This method will run a {@link Executable} instance
     * Use when Operation will throw an exception
     * </p>
     *
     * @param executable {@link Runnable} type variable
     * @param <T>        Type of value
     * @return returns new instance of {@link Que}
     * @throws Exception this can be any exception throw when executing
     * @since 1.0
     */
    public static <T> Que<T> execute(Executable executable) throws Exception {
        executable.execute();
        return Que.getInstance();
    }

    /**
     * <p>
     * This method will run a {@link Executable} instance
     * Use when Operation will throw an exception
     * </p>
     *
     * @param executable {@link Executable} type variable
     * @return existing instance of {@link Que}
     * @throws Exception instance of any exception thrown.
     * @since 1.0
     */
    public Que<T> andExecute(Executable executable) throws Exception {
        executable.execute();
        return this;
    }

    /**
     * <p>This method will supply data {@link Que#value} variable</p>
     *
     * @param supplier {@link Supplier}  variable
     * @return existing instance of {@link Que}
     * @since 1.0
     **/
    public Que<T> andSupply(Supplier<T> supplier) {
        return Que.createReference(supplier.get());
    }

    /**
     * <p>
     * This method will deal data {@link Que#value} variable.
     * Use this method in place of {@link Supplier} if operation will throw an {@link Exception}.
     * </p>
     *
     * @param dealer {@link Dealer}  variable
     * @return existing instance of {@link Que}
     * @since 2.0
     **/
    public Que<T> andDeal(Dealer<T> dealer) throws Exception {
        return Que.createReference(dealer.deal());
    }

    /**
     * <p>This method will consume a {@link Consumer} type variable</p>
     *
     * @param consumer {@link Consumer} type variable
     * @return existing instance of {@link Que}
     * @since 1.0
     */
    public Que<T> andConsume(Consumer<T> consumer) {
        consumer.accept(this.value);
        return this;
    }

    /**
     * <p>
     * This method will accept na {@link Accepter} type variable.
     * Use when operation will/may throw and {@link Exception}
     * </p>
     *
     * @param accepter {@link Consumer} type variable
     * @return existing instance of {@link Que}
     * @since 1.0
     */
    public Que<T> andAccept(Accepter<T> accepter) throws Exception {
        accepter.accept(this.value);
        return this;
    }

    /**
     * <p>This method will execute a {@link Runnable} type variable</p>
     *
     * @param runnable {@link Runnable} type variable
     * @return existing instance of {@link Que}
     * @since 1.0
     */
    public Que<T> andRun(Runnable runnable) {
        runnable.run();
        return this;
    }

    /**
     * <p>This method will execute {@link Callable} type variable</p>
     *
     * @param callable {@link Callable} type variable.
     * @return the result type of method {@code call}
     * @throws Exception instance of any exception thrown.
     * @since 1.0
     */
    public Que<T> andCall(Callable<T> callable) throws Exception {
        return Que.createReference(callable.call());
    }

    /**
     * <p>This method returns {@link Que#value}</p>
     *
     * @return {@link Que#value}
     * @since 1.0
     */
    public T get() {
        return this.value;
    }

    /**
     * <p>This method returns {@link CompletableFuture} of {@link Que#value}</p>
     *
     * @return {@link CompletableFuture} of {@link Que#value}
     * @since 1.0
     */
    public CompletableFuture<T> completableFuture() {
        return CompletableFuture.completedFuture(this.value);
    }

    /**
     * <p>This method returns {@link Optional} of {@link Que#value}</p>
     *
     * @return {@link Optional} of {@link Que#value}
     * @since 3.0
     */
    public Optional<T> optional() {
        return Optional.ofNullable(this.value);
    }
}
