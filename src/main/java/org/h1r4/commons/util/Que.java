/*
 * Copyright (C) 2018 — 2019 Honerfor, Inc.
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

package org.h1r4.commons.util;

import org.h1r4.commons.util.function.Executable;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * <P>
 * This is class called {@link Que} gotten from the word Cue.
 * This class will help give signals to operation..Use appropriately.
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
     * Constructor
     *
     * @param value value of T type.
     * @since 1.0
     */
    private Que(T value) {
        this.value = requireNonNull(value);
    }

    /**
     * Default private constructor.
     *
     * @since 1.0
     */
    private Que() {

    }

    /**
     * This method will create a new instance of  {@link Que} class
     *
     * @param value {@link Que} value
     * @param <T>   value Type
     * @return Returns instance of {@link Que}
     */
    public static <T> Que<T> of(T value) {
        return new Que<>(value);
    }

    /**
     * This method will execute {@link Consumer} type variable.
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
     * This method will run a {@link Runnable} instance.
     *
     * @param runnable {@link Runnable} type variable
     * @param <T>      @ <T> value Type
     * @return returns new instance of {@link Que}
     * @since 1.0
     */
    public static <T> Que<T> run(Runnable runnable) {
        runnable.run();
        return new Que<>();
    }

    /**
     * This method will run a {@link Executable} instance
     * Use when Operation will throw an exception
     *
     * @param executable {@link Runnable} type variable
     * @param <T>        value Type
     * @return returns new instance of {@link Que}
     * @since 1.0
     */
    public static <T> Que<T> execute(Executable executable) throws Exception {
        executable.execute();
        return new Que<>();
    }

    /**
     * This method will run a {@link Executable} instance
     * Use when Operation will throw an exception
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
     * This method will asi supply data {@link Que#value} variable
     *
     * @param supplier {@link Supplier}  variable
     * @return existing instance of {@link Que}
     * @since 1.0
     **/
    public Que<T> andSupply(Supplier<T> supplier) {
        this.value = requireNonNull(supplier.get());
        return this;
    }

    /**
     * This method will accept a {@link Consumer} type variable of type
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
     * This method will execute a {@link Runnable} type variable
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
     * This method will execute {@link Callable} type variable of type.
     *
     * @param callable {@link Callable} type variable.
     * @return the result type of method {@code call}
     * @throws Exception instance of any exception thrown.
     * @since 1.0
     */
    public Que<T> andCall(Callable<T> callable) throws Exception {
        this.value = requireNonNull(callable.call());
        return this;
    }

    /**
     * This method returns {@link Que#value}
     *
     * @return {@link Que#value}
     * @since 1.0
     */
    public T get() {
        return this.value;
    }

    /**
     * This method returns {@link CompletableFuture} of {@link Que#value}
     *
     * @return {@link CompletableFuture} of {@link Que#value}
     * @since 1.0
     */
    public CompletableFuture<T> completableFuture() {
        return completedFuture(this.value);
    }
}
