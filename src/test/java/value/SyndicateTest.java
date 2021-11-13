/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2021 Bobai Kato. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import art.cutils.value.Pause;
import art.cutils.value.Syndicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Created by B0BAI on 13 Nov, 2021 */
final class SyndicateTest {

  @Test
  void testProcessing() {

    final List<Integer> numbers =
        new ArrayList<Integer>() {
          {
            add(1);
            add(2);
            add(3);
          }
        };

    final Set<Object> results =
        new HashSet<Object>() {
          {
            add(6);
            add(14);
            add("aeroplanes");
          }
        };

    try (final Syndicate<Object> syndicate = Syndicate.init()) {
      syndicate
          .add(() -> numbers.stream().mapToInt(i -> i).sum())
          .add(() -> numbers.stream().mapToInt(i -> i * i).sum())
          .add(
              () -> {
                Pause.until(1).seconds();
                return "aeroplanes";
              })
          .execute()
          .onComplete(
              futures -> {
                for (final Future<?> f : futures) {
                  Assertions.assertTrue(results.contains(f.get()));
                  Assertions.assertEquals(futures.size(), results.size());
                }
              });

    } catch (Exception e) {
      e.printStackTrace();
    }

    try (final Syndicate<Object> syndicate = Syndicate.init(Executors.newFixedThreadPool(2))) {
      syndicate
          .add(() -> numbers.stream().mapToInt(i -> i).sum())
          .add(() -> numbers.stream().mapToInt(i -> i * i).sum())
          .add(
              () -> {
                Pause.until(1).seconds();
                return "aeroplanes";
              })
          .execute()
          .setTimeOut(3L, TimeUnit.SECONDS)
          .onComplete(
              futures -> {
                for (final Future<?> f : futures) {
                  Assertions.assertTrue(results.contains(f.get()));
                  Assertions.assertEquals(futures.size(), results.size());
                }
              });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testContracts() {
    final Syndicate<?> s1 = Syndicate.init();
    final Syndicate<?> s2 = Syndicate.init(Executors.newFixedThreadPool(1));

    Assertions.assertNotEquals(s1, s2);
    Assertions.assertNotEquals(s1, "");
    Assertions.assertEquals(s1, s1);
    Assertions.assertEquals(s2, s2);

    Assertions.assertNotEquals(s2, s1);

    Assertions.assertNotEquals(s1.toString(), s2.toString());
    Assertions.assertNotEquals(s1.hashCode(), s2.hashCode());

    Assertions.assertEquals(s1.execute(), s1.execute());
    Assertions.assertEquals(s2.execute(), s2.execute());

    Assertions.assertNotEquals(s1.execute(), "");
    Assertions.assertNotEquals(s1.execute(), s2.execute());
    Assertions.assertNotEquals(s1.execute(), s2.execute());

    Assertions.assertNotEquals(s2.execute(), s1.execute());
    Assertions.assertNotEquals(s1.execute().toString(), s2.execute().toString());
    Assertions.assertNotEquals(s1.execute().hashCode(), s2.execute().hashCode());
  }
}
