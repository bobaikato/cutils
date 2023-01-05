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

package functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import art.cutils.function.TriConsumer;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

final class TriConsumerTest {

  @Test
  void verifyAccepterOperations() {
    final AtomicReference<String> reference = new AtomicReference<>();

    final TriConsumer<String, String, String> triConsumer = (x, y, z) -> reference.set(x + y + y);

    final TriConsumer<String, String, String> tc =
        triConsumer.andThen((x, y, z) -> reference.set(x + z + y));

    tc.accept("A", "B", "C");

    assertEquals("ACB", reference.get());
  }
}
