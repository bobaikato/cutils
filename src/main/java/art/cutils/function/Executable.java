/*
 *  _________  ____ ______________.___.____       _________
 *  \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 *  /    \  \/|    |   / |    |   |   |    |     \_____  \
 *  \     \___|    |  /  |    |   |   |    |___  /        \
 *   \______  /______/   |____|   |___|_______ \/_______  /
 *          \/                                \/        \/
 *
 *  Copyright (C) 2018 — 2021 Bobai Kato. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package art.cutils.function;

/**
 * Represent operations that need the {@link Runnable} like behavior. Use the {@link Executable} for
 * operation that may throw and {@link Exception}
 *
 * @author Bobai Kato — https://github.com/B0BAI
 * @see Runnable
 * @since 1.0
 */
@FunctionalInterface
public interface Executable {

  /**
   * Execute the operation that may likely throw and Exception.
   *
   * @throws Exception that is thrown when operation executes.
   */
  void execute() throws Exception;
}
