import org.h1r4.commons.util.Partition;
import org.h1r4.commons.util.Que;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

final class PartitionTest {

    @DisplayName("Should successfully Partition List by the provided sub-list Size.")
    @ParameterizedTest(name = "{index} => partitionSize={0}, list={1}")
    @MethodSource("partitionListInSubListIResource")
    void partitionListInSubListI(int partitionSize, List<?> list) {
        final var partitionedList = Partition.of(list).into(partitionSize);
        Que.run(() -> assertEquals(partitionedList.size(), 3)).andRun(() -> {
            partitionedList.parallelStream().forEach(item -> {
                assertEquals(item.size(), partitionSize);
            });
        });
    }

    private static Stream<Arguments> partitionListInSubListIResource() {
        return Stream.of(
                Arguments.of(3, List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)),
                Arguments.of(2, List.of("A", "B", "C", "D", "E", "F")),
                Arguments.of(4, List.of('a', 'b', 'c', '4', '5', '6', '7', '8', '9', 'd', 'e', 'f')),
                Arguments.of(2, new ArrayList<List<Integer>>() {{
                    add(List.of(1, 2));
                    add(List.of(3, 4));
                    add(List.of(5, 6));
                    add(List.of(7, 8));
                    add(List.of(9, 10));
                    add(List.of(11, 12));
                }})
        );
    }

    @DisplayName("Should successfully Partition List when partition size is greater than Item in List.")
    @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
    @MethodSource("partitionListInSubListIIResource")
    void partitionListInSubListII(int partitionSize, List<?> list) {
        final var partitionedList = Partition.of(list).into(partitionSize);
        Que.run(() -> assertEquals(partitionedList.size(), 1)).andRun(() -> {
            partitionedList.forEach(item -> assertNotEquals(item.size(), partitionSize));
        }).andRun(() -> {
            partitionedList.forEach(item -> assertEquals(item.size(), list.size()));
        });
    }

    private static Stream<Arguments> partitionListInSubListIIResource() {
        return Stream.of(
                Arguments.of(6, List.of(1, 2, 3, 4, 5)),
                Arguments.of(7, List.of("A", "B", "C", "D", "E", "F")),
                Arguments.of(20, List.of('a', 'b', 'c', '4', '5', '6', '9', 'd', 'e'))
        );
    }


    @DisplayName("Should throw NullPointerException  when trying to Partition a null List.")
    @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
    @MethodSource("partitionListNullResource")
    void nullPointerExceptionForNullListPartition(int partitionSize, List<?> list) {
        assertThrows(NullPointerException.class, () -> {
            Partition.of(list).into(partitionSize);
        });
    }

    private static Stream<Arguments> partitionListNullResource() {
        return Stream.of(Arguments.of(1, null), Arguments.of(2, null));
    }

    @DisplayName("Should throw IllegalArgumentException when partitionSize is Less that 1.")
    @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
    @MethodSource("partitionSizeLessThanOneResource")
    void illegalArgumentExceptionForPartition(int partitionSize, List<?> list) {
        assertThrows(IllegalArgumentException.class, () -> {
            Partition.of(list).into(partitionSize);
        });
    }

    private static Stream<Arguments> partitionSizeLessThanOneResource() {
        return Stream.of(
                Arguments.of(0, List.of(1, 2, 3, 4, 5)),
                Arguments.of(-7, List.of("A", "B", "C", "D", "E", "F")),
                Arguments.of(-2, List.of('a', 'b', 'c', 'd'))
        );
    }
}
