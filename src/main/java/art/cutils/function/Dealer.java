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

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Represents a dealer of results. UnLike {@link Supplier} and like {@link Callable}, {@link
 * Dealer#deal()} throw an {@link Exception}. Uses the {@link Dealer} instead of {@link Supplier} if
 * an {@link Exception} will be thrown for the operation prior to the supplying of a result.
 *
 * <p>There is no requirement that the {@link Dealer} will return distinct result when called.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #deal()}.
 *
 * @param <T> the type of results supplied by this supplier.
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @see Callable
 * @see Supplier
 * @since 1.0
 */
@FunctionalInterface
public interface Dealer<T> {

  /**
   * Gets a result.
   *
   * @return a result
   * @throws Exception operation exception thrown
   */
  T deal() throws Exception;
}