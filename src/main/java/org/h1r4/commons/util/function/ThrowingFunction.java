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

package org.h1r4.commons.util.function;

import java.util.function.Function;

/**
 * <p>
 * When you find yourself in a situation where you need to use a method ( that throws an exception)
 * withing Lambda, this class is what you need to achieve your goal without Java complaining.
 * <p>
 * The class Represents a function that accepts one argument and produces a result.
 * whose functional method is {@link #apply(Object)}.
 * </p>
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author B0BAI
 * @since 1.0
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

    /**
     * <p>Applies this function to the given argument.</p>
     *
     * @param t the function argument if Type T
     * @return the function result
     * @throws Exception any excetion thrown when applying function
     */
    R apply(T t) throws Exception;

    @SuppressWarnings("unchecked")
    private static <T extends Exception, R> R sneakyThrow(Exception ex) throws T {
        throw (T) ex;
    }

    /**
     * @param function Variable of {@link ThrowingFunction}
     * @param <T>      the type of the input to the function
     * @param <R>      the type of the result of the function
     * @return A {@link Function}
     */
    static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> function) {
        return argument -> {
            try {
                return function.apply(argument);
            } catch (Exception ex) {
                return sneakyThrow(ex);
            }
        };
    }
}