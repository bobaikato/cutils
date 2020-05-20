/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2020 Honerfor, Inc. All Rights Reserved.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.honerfor.cutils.Partition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test List Partitioning")
final class PartitionTest {

  @DisplayName("Should successfully Partition List by the provided sub-list Size.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, list={1}")
  @MethodSource("partitionListInSubListIResource")
  void partitionListInSubListI(int partitionSize, List<?> list) {
    final Partition<?> partitionedList = Partition.of(list).into(partitionSize);
    assertEquals(partitionedList.size(), 3);
    partitionedList.parallelStream().forEach(item -> assertEquals(item.size(), partitionSize));
  }

  private static Stream<Arguments> partitionListInSubListIResource() {
    return Stream.of(
        Arguments.of(3, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)),
        Arguments.of(2, Arrays.asList("A", "B", "C", "D", "E", "F")),
        Arguments.of(4, Arrays.asList('a', 'b', 'c', '4', '5', '6', '7', '8', '9', 'd', 'e', 'f')),
        Arguments.of(
            2,
            new ArrayList<List<Integer>>() {
              {
                add(Arrays.asList(1, 2));
                add(Arrays.asList(3, 4));
                add(Arrays.asList(5, 6));
                add(Arrays.asList(7, 8));
                add(Arrays.asList(9, 10));
                add(Arrays.asList(11, 12));
              }
            }));
  }

  @DisplayName(
      "Should successfully Partition List when partition size is greater than Item in List.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
  @MethodSource("partitionListInSubListIIResource")
  void partitionListInSubListII(int partitionSize, List<?> list) {
    final Partition<?> partitionedList = Partition.of(list).into(partitionSize);
    assertEquals(partitionedList.size(), 1);
    partitionedList.forEach(item -> assertNotEquals(item.size(), partitionSize));
    partitionedList.forEach(item -> assertEquals(item.size(), list.size()));
  }

  private static Stream<Arguments> partitionListInSubListIIResource() {
    return Stream.of(
        Arguments.of(6, Arrays.asList(1, 2, 3, 4, 5)),
        Arguments.of(7, Arrays.asList("A", "B", "C", "D", "E", "F")),
        Arguments.of(20, Arrays.asList('a', 'b', 'c', '4', '5', '6', '9', 'd', 'e')));
  }

  @DisplayName("Should throw NullPointerException  when trying to Partition a null List.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
  @MethodSource("partitionListNullResource")
  void nullPointerExceptionForNullListPartition(int partitionSize, List<?> list) {
    assertThrows(NullPointerException.class, () -> Partition.of(list).into(partitionSize));
  }

  private static Stream<Arguments> partitionListNullResource() {
    return Stream.of(Arguments.of(1, null), Arguments.of(2, null));
  }

  @DisplayName("Should throw IllegalArgumentException when partitionSize is Less that 1.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
  @MethodSource("partitionSizeLessThanOneResource")
  void illegalArgumentExceptionForPartition(int partitionSize, List<?> list) {
    assertThrows(IllegalArgumentException.class, () -> Partition.of(list).into(partitionSize));
  }

  private static Stream<Arguments> partitionSizeLessThanOneResource() {
    return Stream.of(
        Arguments.of(0, Arrays.asList(1, 2, 3, 4, 5)),
        Arguments.of(-7, Arrays.asList("A", "B", "C", "D", "E", "F")),
        Arguments.of(-2, Arrays.asList('a', 'b', 'c', 'd')));
  }
}
