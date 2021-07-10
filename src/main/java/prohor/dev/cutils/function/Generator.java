/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2021 Prohorde, LTD. All Rights Reserved.
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

package prohor.dev.cutils.function;

/**
 * Represent operations that will return unique/generated values every time {@link
 * Generator#generate()} is called. Please note that the onus is on you to provide the
 * implementation that will return the unique values.Else {@link Generator} Function will have the
 * same behavior as the {@link Dealer} and {@link Runnable}.
 *
 * @param <T> type of value.
 * @author Bobai Kato <https://github.com/B0BAI>
 * @see Runnable
 * @see Dealer
 * @since 2.0
 */
@FunctionalInterface
public interface Generator<T> {

  /**
   * Generates values.
   *
   * @return return generate values of T type.
   * @since 2.o
   */
  T generate();
}
