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

package art.cutils.value;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import art.cutils.function.Dealer;
import art.cutils.function.Executable;

/**
 * Pause allows you to write a safe an idiomatic expression to pause an execution on the current
 * execution thread.
 *
 * @param <T> type.
 * @author Bobai Kato {https://github.com/B0BAI}
 * @since 7.0
 */
public final class Pause<T> {

  /**
   * This will hold instance of {@link Run} to serve other run operations.
   *
   * @since 7.0
   */
  private final Run<T> run;

  /** Sealed. Default constructor to set time out number */
  private Pause(final int timeOut) {
    this.run = new Run<>(timeOut);
  }

  /**
   * Until is used to set the timeout number, which will be interpreted into it's subsequent time
   * equivalent
   *
   * @param timeOut Number to be interpreted to time
   * @param <T> the type of the operation
   * @return an instance of {@link Pause} for subsequent operations.
   */
  public static <T> Pause<T> until(final int timeOut) {
    return new Pause<>(timeOut);
  }

  /**
   * Set {@link Run} time unit to {@link TimeUnit#NANOSECONDS}
   *
   * @return instance of {@link Run} of the set type T
   */
  public Run<T> nanoSeconds() {
    return this.run.setTimeUnit(NANOSECONDS);
  }

  /**
   * Set {@link Run} time unit to {@link TimeUnit#MICROSECONDS}
   *
   * @return instance of {@link Run} of the set type T
   */
  public Run<T> microSeconds() {
    return this.run.setTimeUnit(MICROSECONDS);
  }

  /**
   * Set {@link Run} time unit to {@link TimeUnit#MILLISECONDS}
   *
   * @return instance of {@link Run} of the set type T
   */
  public Run<T> milliSeconds() {
    return this.run.setTimeUnit(MILLISECONDS);
  }

  /**
   * Set {@link Run} time unit to {@link TimeUnit#SECONDS}
   *
   * @return instance of {@link Run} of the set type T
   */
  public Run<T> seconds() {
    return this.run.setTimeUnit(SECONDS);
  }

  /**
   * Set {@link Run} time unit to {@link TimeUnit#MINUTES}
   *
   * @return instance of {@link Run} of the set type T
   */
  public Run<T> minute() {
    return this.run.setTimeUnit(MINUTES);
  }

  /**
   * Set {@link Run} time unit to {@link TimeUnit#HOURS}
   *
   * @return instance of {@link Run} of the set type T
   */
  public Run<T> hours() {
    return this.run.setTimeUnit(HOURS);
  }

  /**
   * Set {@link Run} time unit to {@link TimeUnit#DAYS}
   *
   * @return instance of {@link Run} of the set type T
   */
  public Run<T> days() {
    return this.run.setTimeUnit(DAYS);
  }

  /**
   * This class is responsible for executing the operations base on the set time interpreted to the
   * requested unit.
   *
   * @param <T> type.
   */
  public static final class Run<T> {

    /** Number to be interpreted to time */
    private final int timeOut;

    /** Instance of {@link TimeUnit} that is use to interpret the specified time out value. */
    private TimeUnit timeUnit;

    /**
     * Sealed. Default run constructor to set timeout value.
     *
     * @param timeOut Number to be interpreted to time
     */
    private Run(final int timeOut) {
      this.timeOut = timeOut;
    }

    /**
     * @param timeUnit Instance of {@link TimeUnit} that is use to interpret the specified time out
     *     value
     * @return instance of {@link Run} of the set type.
     */
    private Run<T> setTimeUnit(final TimeUnit timeUnit) {
      this.timeUnit = timeUnit;
      return this;
    }

    /**
     * This method chains a {@link Executable} expression that execute after the pause.
     *
     * @param executable this method take an expressions {@link Executable} that has no return
     *     value.
     * @return an instance of {@link Delay} for further operations.
     */
    public Delay<T> thenRun(final Executable executable) {
      return new Delay<>(this.timeOut, this.timeUnit, executable);
    }

    /**
     * This method chains a {@link Dealer} expression that execute after the pause.
     *
     * @param dealer this method take an expressions {@link Dealer} that has a return value.
     * @return an instance of {@link Delay} operations.
     */
    public Delay<T> thenRun(final Dealer<T> dealer) {
      return new Delay<>(this.timeOut, this.timeUnit, dealer);
    }

    /**
     * This is empty, if you don't intend to chain any expression
     *
     * @return an instance of {@link Delay} operations.
     */
    public Delay<T> empty() {
      return new Delay<>(this.timeOut, this.timeUnit);
    }

    /**
     * This class execute the delay and cause the pause before the set operations executed.
     *
     * @param <T> type
     */
    public static final class Delay<T> {

      /** Holds the try result of the {@link Try} operation. */
      private final Try<T> tryResult;

      /**
       * Seal constructor for no expression.
       *
       * @param timeOut Number to be interpreted to time
       * @param timeUnit Instance of {@link TimeUnit} that is use to interpret the specified time
       *     out value
       */
      private Delay(final int timeOut, final TimeUnit timeUnit) {
        this.tryResult = Try.of(() -> timeUnit.sleep(timeOut));
      }

      /**
       * Seal constructor for the {@link Executable} expressions.
       *
       * @param timeOut Number to be interpreted to time
       * @param timeUnit Instance of {@link TimeUnit} that is use to interpret the specified time
       *     out value
       * @param executable {@link Executable} instance with the operations to be executed after the
       *     pause.
       */
      private Delay(final int timeOut, final TimeUnit timeUnit, final Executable executable) {
        this.tryResult =
            Try.of(
                () -> {
                  timeUnit.sleep(timeOut);
                  executable.execute();
                });
      }

      /**
       * Seal constructor for the {@link Dealer} expressions.
       *
       * @param timeOut Number to be interpreted to time
       * @param timeUnit Instance of {@link TimeUnit} that is use to interpret the specified time
       *     out value
       * @param dealer {@link Dealer} instance with the operations to be executed after the pause.
       */
      private Delay(final int timeOut, final TimeUnit timeUnit, final Dealer<T> dealer) {
        this.tryResult =
            Try.of(
                () -> {
                  timeUnit.sleep(timeOut);
                  return dealer.deal();
                });
      }

      /**
       * This gets the {@link Try} operation result.
       *
       * @return try result. Instance of {@link Try}
       */
      public Try<T> get() {
        return this.tryResult;
      }

      /**
       * Takes a consumer that consumes the {@link Try} operation results.
       *
       * @param tryResult {@link Try} operation results.
       */
      public void onComplete(final Consumer<Try<T>> tryResult) {
        tryResult.accept(this.tryResult);
      }

      @Override
      public boolean equals(final Object o) {
        if (this == o) {
          return true;
        }
        if (o instanceof Delay) {
          final Delay<?> delay = (Delay<?>) o;
          return tryResult.equals(delay.tryResult);
        } else {
          return false;
        }
      }

      @Override
      public int hashCode() {
        return Objects.hash(tryResult);
      }
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof Run) {
        final Run<?> run = (Run<?>) o;
        return timeOut == run.timeOut && timeUnit == run.timeUnit;
      } else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return Objects.hash(timeOut, timeUnit);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Pause) {
      final Pause<?> pause = (Pause<?>) o;
      return run.equals(pause.run);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(run);
  }
}
