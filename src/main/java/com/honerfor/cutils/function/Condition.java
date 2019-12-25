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

package com.honerfor.cutils.function;

/**
 * <p>
 * Represents an operation that returns a condition/boolean.
 * whose functional method is {@link #isMet()}.
 * </p>
 *
 * @author B0BAI
 * @since 1.0
 */
@FunctionalInterface
public interface Condition {

    /**
     * <p>Check the condition</p>
     *
     * @return the final condition of boolean
     * @since 1.0
     */
    boolean isMet();

    /**
     * <p>
     * Negates the condition. True is condition isn't met and False when condition is met.</p>
     *
     * @return the final negation of the actual condition of boolean.
     * @since 3.0
     */
    default boolean isNotMet() {
        return !this.isMet();
    }
}
