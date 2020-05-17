package functions;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.honerfor.cutils.function.Dealer;
import com.honerfor.cutils.function.Idler;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class IdlerTest {

  static final int MOCK_LATENCY = 2000;

  @DisplayName("Expect Idler to memoize values for Dealers.")
  @ParameterizedTest(name = "{index} =>  value={1}")
  @MethodSource("idlerDealerOperations")
  void verifyIdlerMemoizedDealerValues(final Dealer<Integer> dealer, final long sec)
      throws Exception {
    final long startTime = nanoTime();

    final int result = dealer.deal();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    Assertions.assertEquals(MOCK_LATENCY, result); // result check
    Assertions.assertEquals(sec, executionTime); // check execution time
  }

  private static Stream<Arguments> idlerDealerOperations() {

    final Dealer<Integer> dealer =
        Idler.deal(
            () -> {
              final int time = MOCK_LATENCY;
              Thread.sleep(time); // mock operation with high latency
              return time;
            });

    return Stream.of(
        Arguments.of(dealer, 2),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0),
        Arguments.of(dealer, 0));
  }

  @DisplayName("Expect Idler to memoize values for Suppliers.")
  @ParameterizedTest(name = "{index} =>  value={1}")
  @MethodSource("idlerSupplierOperations")
  void verifyIdlerMemoizedSupplierValues(final Supplier<Integer> supplier, final long sec) {
    final long startTime = nanoTime();

    final int result = supplier.get();

    final long endTime = nanoTime();
    final long executionTime = SECONDS.convert((endTime - startTime), NANOSECONDS);

    Assertions.assertEquals(MOCK_LATENCY, result); // result check
    Assertions.assertEquals(sec, executionTime); // check execution time
  }

  private static Stream<Arguments> idlerSupplierOperations() {

    final Supplier<Integer> supplier =
        Idler.supply(
            () -> {
              final int time = MOCK_LATENCY;
              try {
                Thread.sleep(time); // mock operation with high latency
              } catch (InterruptedException e) {
              }
              return time;
            });

    return Stream.of(
        Arguments.of(supplier, 2),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0),
        Arguments.of(supplier, 0));
  }
}
