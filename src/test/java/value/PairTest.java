/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2022 Bobai Kato. All Rights Reserved.
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

package value;

import art.cutils.value.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PairTest {

  @Test
  void testPairWithInitialValueOnCreation() {
    final Pair<Integer, String> pair = Pair.of(1, "Two");
    Assertions.assertEquals(1, pair.getFirst());
    Assertions.assertEquals("Two", pair.getSecond());

    Assertions.assertTrue(pair.hasFirst());
    Assertions.assertTrue(pair.hasSecond());

    Assertions.assertTrue(pair.isNotEmpty());
  }

  @Test
  void testInitialEmptyPair() {
    final Pair<Integer, String> pair = Pair.empty();

    Assertions.assertNull(pair.getFirst());
    Assertions.assertNull(pair.getSecond());

    Assertions.assertFalse(pair.hasFirst());
    Assertions.assertFalse(pair.hasSecond());

    Assertions.assertFalse(pair.isNotEmpty());

    Assertions.assertTrue(pair.notHasFirst());
    Assertions.assertTrue(pair.notHasSecond());

    Assertions.assertTrue(pair.isEmpty());

    Assertions.assertEquals(1, pair.first(1).second("Two").getFirst());
    Assertions.assertEquals("Two", pair.getSecond());

    Assertions.assertTrue(pair.hasFirst());
    Assertions.assertTrue(pair.hasSecond());

    Assertions.assertTrue(pair.isNotEmpty());
  }

  @Test
  void testPauseContracts() {
    final Pair<Integer, String> pair = Pair.of(1, "Two");
    final Pair<String, String> pair2 = Pair.of("One", "Two");

    Assertions.assertNotEquals(pair, pair2);

    Assertions.assertFalse(pair.equals(null) && pair.equals(new Object()));

    Assertions.assertEquals(pair, Pair.of(1, "Two"));

    Assertions.assertEquals(pair.hashCode(), Pair.of(1, "Two").hashCode());

    Assertions.assertNotEquals(pair.hashCode(), pair2.hashCode());

    Assertions.assertNotSame(pair.toString(), pair2.toString());
  }

  @Test
  void testPairSwapping(){
    final Pair<Integer, String> pair = Pair.of(1, "Two");
    final Pair<String, Integer> pair2 = Pair.of("Two", 1);

    Assertions.assertEquals(pair2, pair.swap());
  }

  @Test
  void testPairWithNullValues() {
    final Pair<Integer, String> pair = Pair.of(1, null);
    Assertions.assertEquals(1, pair.getFirst());
    Assertions.assertNull(pair.getSecond());

    Assertions.assertTrue(pair.hasFirst());
    Assertions.assertFalse(pair.hasSecond());

    Assertions.assertTrue(pair.isNotEmpty());

    Assertions.assertNull(pair.first(null).getFirst());
    Assertions.assertEquals("Two", pair.second("Two").getSecond());

    Assertions.assertFalse(pair.hasFirst());
    Assertions.assertTrue(pair.hasSecond());

    Assertions.assertTrue(pair.isNotEmpty());
  }

  @Test
  void testResetAndDelete() {
    final Pair<Integer, String> pair = Pair.of(1, "Two");

    Assertions.assertNull(pair.clear().getFirst());
    Assertions.assertNull(pair.getSecond());
    Assertions.assertFalse(pair.hasFirst());
    Assertions.assertFalse(pair.hasSecond());
    Assertions.assertFalse(pair.isNotEmpty());
    Assertions.assertTrue(pair.isEmpty());

    pair.setFirstAndSecond(3, "Four");

    Assertions.assertEquals(3, pair.getFirst());
    Assertions.assertEquals("Four", pair.getSecond());

    Assertions.assertTrue(pair.hasFirst());
    Assertions.assertTrue(pair.hasSecond());

    Assertions.assertTrue(pair.isNotEmpty());
  }
}
