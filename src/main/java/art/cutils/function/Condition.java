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

/**
 * Represents an operation that returns a condition/boolean. whose functional method is {@link
 * #isMet()}.
 *
 * @author @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @since 1.0
 */
@FunctionalInterface
public interface Condition {

  /**
   * Negates the condition. True is condition isn't met and False when condition is met.
   *
   * @return the final negation of the actual condition of boolean.
   * @since 1.0
   */
  default boolean isNotMet() {
    return !this.isMet();
  }

  /**
   * Check the condition.
   *
   * @return the final condition of boolean
   * @since 1.0
   */
  boolean isMet();
}