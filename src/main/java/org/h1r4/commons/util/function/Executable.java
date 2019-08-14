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

package org.h1r4.common.function;

/**
 * <p>
 * Represent operations that need the {@link Runnable}
 * like behavior with the throw exception option.
 * </p>
 *
 * @author B0BAI
 * @since 1.0
 */
@FunctionalInterface
public interface Executable {

    /**
     * <p>Execute the operation that may likely throw and Exception.</p>
     *
     * @throws Exception that is thrown when operation executes.
     */
    void execute() throws Exception;
}