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
package value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import art.cutils.value.Que;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

final class QueTest {

  @Test
  void testUsingQueAsVariableHolder() throws Exception {
    final AtomicReference<String> reference = new AtomicReference<>();

    Que.of("Hello World 1")
        .andConsume(
            value -> {
              final StringBuilder sb = new StringBuilder();
              sb.append(value);
              reference.set(sb.reverse().toString());
            });

    assertEquals("1 dlroW olleH", reference.get());

    final List<String> list =
        Que.<ArrayList<String>>of(new ArrayList<>())
            .andConsume(
                al -> {
                  al.add("Donald");
                  al.add("Trump");
                })
            .andAccept(
                al -> {
                  al.remove(0);
                  al.add("President");
                })
            .get();

    assertEquals(2, list.size());
    assertEquals("Trump", list.get(0), "First List value should be: Trump");
    assertEquals("President", list.get(1), "Second List value should be: President");
  }

  @Test
  void testQueShouldReturnAnOptionalValueType() {
    final Optional<ArrayList<String>> optional =
        Que.<ArrayList<String>>of(new ArrayList<>())
            .andConsume(
                al -> {
                  al.add("Donald");
                  al.add("Trump");
                })
            .optional();

    assertNotNull(optional);
    assertTrue(optional instanceof Optional, "Should be of Optional Type");
  }

  @Test
  void testQueShouldReturnAnCompletableFutureValueType() {
    final CompletableFuture<ArrayList<String>> result =
        Que.<ArrayList<String>>of(new ArrayList<>())
            .andConsume(
                al -> {
                  al.add("Donald");
                  al.add("Trump");
                })
            .completableFuture();

    assertNotNull(result);
    assertTrue(result instanceof CompletableFuture, "Should be of CompletableFuture Type");
  }

  @Test
  void testQueShouldPerformOperationAndSupplyAResult() throws Exception {
    final String result =
        Que.<String>run(
                () -> {
                  // ..  Run some operations ..
                })
            .andRun(
                () -> {
                  // ..Run some operations with no return type ..
                })
            .andExecute(
                () -> {
                  // ..  Execute some operations that may throw exception with no return type ..
                })
            .andSupply(
                () -> {
                  // .. Perform some Operations ..
                  return "a string";
                })
            .get();

    assertNotNull(result);
    assertEquals("a string", result, "Should return 'a string'");

    final String anotherResult =
        Que.<String>execute(
                () -> {
                  // ..  Execute some operations that may throw exception with no return type ..
                })
            .andRun(
                () -> {
                  // ..Run some operations with no return type ..
                })
            .andExecute(
                () -> {
                  // ..  Execute some operations that may throw exception with no return type ..
                })
            .andCall(
                () -> {
                  // .. Perform some Operations that has a return type that may throw an exception
                  // ..
                  return "a string";
                })
            .run(
                value -> {
                  // .. use the return value before it's finally accessed
                })
            .andDeal(
                () -> {
                  // .. override result of callable
                  return "another String";
                })
            .get();

    assertNotEquals("a string", anotherResult);
    assertEquals("another String", anotherResult);
  }

  @Test
  void testingOtherVariantOfQueOperation() throws Exception {
    final AtomicReference<String> result =
        Que.as(
                () -> {
                  // .. accepts a Dealer function and return the return of the Ops
                  return new AtomicReference<>("deal result");
                })
            .execute(
                value -> {
                  // .. use deal result
                  value.set(value.get().toUpperCase());
                })
            .get();

    assertEquals("DEAL RESULT", result.get());
  }

  @Test
  void equalsAndHashCodeContractToBeValid() throws Exception {

    Que<String> q1 =
        Que.of(
            () -> {
              // ..supply
              return "Hello World";
            });

    Que<String> q2 =
        Que.<String>run(() -> {})
            .andRun(() -> {})
            .andSupply(
                () -> {
                  // ..supply
                  return "Hello World";
                });

    assertEquals(q1, q2);
    assertEquals(q1, q1);
    assertEquals(q2, q2);

    assertEquals(q1.hashCode(), q2.hashCode());
  }

  @Test
  void equalsAndHashCodeContractToBeInvalid() throws Exception {

    Que<String> q1 =
        Que.of(
            () -> {
              // ..supply
              return "Content";
            });

    Que<String> q2 =
        Que.as(
            () -> {
              // ..supply
              return "Different Content";
            });

    assertNotEquals(q1, q2);
    assertNotEquals(q1, "q2");
    assertNotEquals(q1.hashCode(), q2.hashCode());
  }
}
