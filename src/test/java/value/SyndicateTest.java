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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import art.cutils.value.Pause;
import art.cutils.value.Syndicate;
import art.cutils.value.Syndicate.Close;
import art.cutils.value.Try;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Created by B0BAI on 13 Nov, 2021 */
final class SyndicateTest {

  private final List<Integer> numbers =
      new ArrayList<Integer>() {
        {
          add(1);
          add(2);
          add(3);
        }
      };

  private final Set<Object> results =
      new HashSet<Object>() {
        {
          add(6);
          add(14);
          add("aeroplanes");
        }
      };

  @Test
  void testProcessingWithinTryResourceSuccess() {

    try (final Syndicate<Object> syndicate = Syndicate.init()) {
      syndicate
          .add(() -> numbers.stream().mapToInt(i -> i).sum())
          .add(() -> numbers.stream().mapToInt(i -> i * i).sum())
          .add(
              () -> {
                Pause.until(1).seconds().empty();
                return "aeroplanes";
              })
          .execute()
          .onComplete(
              futuresTry -> {
                Assertions.assertTrue(futuresTry.isSuccess());
                final List<Future<Object>> futures = futuresTry.get();
                for (final Future<Object> future : futures) {
                  try {
                    Assertions.assertTrue(results.contains(future.get()));
                  } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                  }
                  Assertions.assertEquals(futuresTry.get().size(), results.size());
                }
              });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testProcessingWithinTryResourceWithExceptionThrown() {
    try (final Syndicate<Object> syndicate = Syndicate.init(Executors.newFixedThreadPool(2))) {
      syndicate
          .add(() -> numbers.stream().mapToInt(i -> i).sum())
          .add(() -> numbers.stream().mapToInt(i -> i * i).sum())
          .add(
              () -> {
                Pause.until(1).seconds().empty();
                return "aeroplanes";
              })
          .execute()
          .setTimeOut(1L, TimeUnit.MILLISECONDS)
          .onComplete(
              futuresTry -> {
                Assertions.assertTrue(futuresTry.isSuccess());
                final List<Future<Object>> futures = futuresTry.get();
                for (final Future<Object> future : futures) {
                  try {
                    Assertions.assertTrue(results.contains(future.get()));
                  } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                  }
                  Assertions.assertEquals(futuresTry.get().size(), results.size());
                }
              });

    } catch (Exception e) {
      Assertions.assertTrue(e instanceof CancellationException);
    }
  }

  @Test
  void testingProcessingOutSideTryResource() {
    final Try<?> aTry =
        Try.of(
            () ->
                Syndicate.init()
                    .add(() -> numbers.stream().mapToInt(i -> i).sum())
                    .add(() -> numbers.stream().mapToInt(i -> i * i).sum())
                    .add(
                        () -> {
                          Pause.until(1).seconds().empty();
                          return "aeroplanes";
                        })
                    .execute()
                    .onComplete(
                        futuresTry -> {
                          Assertions.assertTrue(futuresTry.isSuccess());
                          final List<Future<Object>> futures = futuresTry.get();
                          for (final Future<Object> future : futures) {
                            try {
                              Assertions.assertTrue(results.contains(future.get()));
                            } catch (InterruptedException | ExecutionException e) {
                              e.printStackTrace();
                            }
                            Assertions.assertEquals(futuresTry.get().size(), results.size());
                          }
                        })
                    .close());

    Assertions.assertTrue(aTry.isSuccess());
  }

  @Test
  void testingRetrievalOfResourceOutsideOnComplete() {
    final @NotNull Try<List<Future<Object>>> aTry =
        Syndicate.init()
            .add(() -> numbers.stream().mapToInt(i -> i).sum())
            .add(() -> numbers.stream().mapToInt(i -> i * i).sum())
            .add(
                () -> {
                  Pause.until(1).seconds().empty();
                  return "aeroplanes";
                })
            .execute()
            .get();

    Assertions.assertTrue(aTry.isSuccess());
    Assertions.assertEquals(3, aTry.get().size());
  }

  @Test
  void testingProcessingOutSideTryResourceWithExceptionThrown() {
    final Try<?> aTry =
        Try.of(
            () -> {
              Syndicate.init()
                  .add(() -> numbers.stream().mapToInt(i -> i).sum())
                  .add(() -> numbers.stream().mapToInt(i -> i * i).sum())
                  .add(
                      () -> {
                        Pause.until(1).seconds().empty();
                        return "aeroplanes";
                      })
                  .execute()
                  .setTimeOut(1L, TimeUnit.MILLISECONDS)
                  .onComplete(
                      futuresTry -> {
                        Assertions.assertTrue(futuresTry.isSuccess());
                        final List<Future<Object>> futures = futuresTry.get();
                        for (final Future<Object> future : futures) {
                          try {
                            Assertions.assertTrue(results.contains(future.get()));
                          } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                          }
                          Assertions.assertEquals(futuresTry.get().size(), results.size());
                        }
                      })
                  .close();
            });
    Assertions.assertTrue(aTry.isFailure());
    Assertions.assertTrue(aTry.getCause() instanceof CancellationException);
  }

  @Test
  void testContracts() throws Exception {
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

    final Close<?> close = s1.execute().onComplete(futures -> {});
    Assertions.assertNotEquals(close.toString(), "");
    Assertions.assertNotEquals(close, s2.execute());
    Assertions.assertNotEquals(close, s2.execute().onComplete(futures -> {}));
    Assertions.assertEquals(close, s1.execute().onComplete(futures -> {}));
  }
}