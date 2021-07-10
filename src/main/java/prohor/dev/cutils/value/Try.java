/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2021 Prohorde, LTD. All Rights Reserved.
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

package prohor.dev.cutils.value;

import prohor.dev.cutils.function.Dealer;
import prohor.dev.cutils.function.Executable;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The {@link Try} class offers the ability write safe code without focusing on try-catch blocks in
 * the presence of exceptions.
 *
 * <p>There are 3 states that determines the functionality of {@link Try} from the try operation
 * performed.
 *
 * <ul>
 *   <li>Successful Operation with result.
 *   <li>Successful Operation without result.
 *   <li>Failed Operation.
 * </ul>
 *
 * <p>Requesting for a result on a successful operation which returns no result or failed operation
 * which has not result will throw an {@link IllegalStateException}, using the {@link
 * Try#map(Function)} will vary depending on the state of the operation ... and so on. Review the
 * methods description for more information.
 *
 * <p>Furthermore, fatal exceptions aren't handle by {@link Try}:
 *
 * <ul>
 *   <li>{@link InterruptedException}
 *   <li>{@link LinkageError}
 *   <li>{@link ThreadDeath}
 *   <li>{@link VirtualMachineError} (i.e. {@link OutOfMemoryError} or {@link StackOverflowError})
 * </ul>
 *
 * @param <T> type
 * @author Bobai Kato
 * @since v5
 */
public abstract class Try<T> implements Serializable {
  private static final long serialVersionUID = 4530258067856316628L;

  /** Locked for life. */
  private Try() {}

  /**
   * Accepts a {@link Dealer} type function which is expected to return a result if operation was
   * successful.
   *
   * @since v5
   * @param operation the operation that will be tried, a variable of {@link Dealer} type.
   * @param <T> variable type
   * @return instance of {@link Try} either with a {@link Success} or {@link Failure} state.
   */
  public static <T> Try<T> of(final Dealer<? extends T> operation) {
    Objects.requireNonNull(operation, "operation cannot be null");
    try {
      return new Success<>(operation.deal());
    } catch (final Exception e) {
      return new Failure<>(e);
    }
  }

  /**
   * Accepts a {@link Executable} type function with no result expected if operation is successful.
   *
   * @param operation the operation that will be tried, a variable of {@link Executable} type.
   * @param <T> variable type
   * @return instance of {@link Try} either with a {@link Success} or {@link Failure} state.
   * @since v5
   */
  public static <T> Try<T> of(final Executable operation) {
    Objects.requireNonNull(operation, "operation cannot be null");
    try {
      operation.execute();
      return new Success<>();
    } catch (final Exception e) {
      return new Failure<>(e);
    }
  }

  /**
   * If try is successful, invoke the specified consumer with the operation result, otherwise do
   * nothing.
   *
   * @apiNote an {@link IllegalStateException} will be thrown if the successful try doesn't return
   *     any result.
   * @param resultConsumer block of operation to be executed with try result
   * @see Success#get()
   * @since v5
   */
  public void onSuccess(final Consumer<? super T> resultConsumer) {
    if (this.isSuccess()) {
      resultConsumer.accept(this.get());
    }
  }

  /**
   * If try operations fails, invoke the specified consumer with the exception thrown during the
   * execution, otherwise do nothing.
   *
   * @param exceptionConsumer operation to be executed if a an exception was thrown
   * @see Failure#getCause()
   * @since v5
   */
  public void onFailure(final Consumer<? super Throwable> exceptionConsumer) {
    if (this.isFailure()) {
      exceptionConsumer.accept(this.getCause());
    }
  }

  /**
   * Perform further operation on successful try {@code result}, if the try fails the second
   * argument block is executed.
   *
   * <p>NOTE: This method will throw an {@link IllegalStateException} if the try operation executed
   * doesn't return a result. Use {@link Success#isSuccess} instead to check the state of the try
   * operation.
   *
   * @param resultConsumer block of operation of {@link Consumer} type to be executed with try
   *     result.
   * @param action block to be executed if try operation fails.
   * @since v5
   */
  public void onSuccessOrElse(final Consumer<? super T> resultConsumer, final Runnable action) {
    if (this.isSuccess()) {
      resultConsumer.accept(this.get());
    } else {
      action.run();
    }
  }

  /**
   * Perform further operation on successful try {@code result}, if the try fails the second
   * argument block is executed.
   *
   * <p>NOTE: This method will throw an {@link IllegalStateException} if the try operation executed
   * doesn't return a result. Use {@link Success#isSuccess} instead to check the state of the try
   * operation.
   *
   * @param resultConsumer block of operation of {@link Consumer} type to be executed with try
   *     result.
   * @param exceptionConsumer block of operation of {@link Consumer} type to be executed when the
   *     try operation succeeds.
   * @since v5
   */
  public void onSuccessOrElse(
      final Consumer<? super T> resultConsumer,
      final Consumer<? super Throwable> exceptionConsumer) {
    if (this.isSuccess()) {
      resultConsumer.accept(this.get());
    } else {
      exceptionConsumer.accept(this.getCause());
    }
  }

  /**
   * Perform further operation on a failed try Exception, if the try succeeds, the second argument
   * block is executed.
   *
   * @param exceptionConsumer block of operation of {@link Consumer} type to be executed when the
   *     try operation succeeds.
   * @param action block to be executed if try operation fails.
   * @since v5
   */
  public void onFailureOrElse(
      final Consumer<? super Throwable> exceptionConsumer, final Runnable action) {
    if (this.isFailure()) {
      exceptionConsumer.accept(this.getCause());
    } else {
      action.run();
    }
  }

  /**
   * Perform further operation on a failed try Exception, if the try succeeds, the second argument
   * block is executed.
   *
   * @param exceptionConsumer block of operation of {@link Consumer} type to be executed when the
   *     try operation succeeds.
   * @param resultConsumer block of operation of {@link Consumer} type to be executed with try
   *     result.
   * @since v5
   */
  public void onFailureOrElse(
      final Consumer<? super Throwable> exceptionConsumer,
      final Consumer<? super T> resultConsumer) {
    if (this.isFailure()) {
      exceptionConsumer.accept(this.getCause());
    } else {
      resultConsumer.accept(this.get());
    }
  }

  /**
   * Use this method to retrieve the try operation result.
   *
   * @return try operation result
   * @throws IllegalStateException Try state is {@link Success} without an available result.
   * @throws UnsupportedOperationException Try state is {@link Failure} when a try operation fails
   * @since v5
   */
  public abstract T get();

  /**
   * Retrieve the Cause of try operation failure.
   *
   * @return exception thrown during try operation.
   * @throws UnsupportedOperationException if try operation is successful
   * @since v5
   */
  public abstract Throwable getCause();

  /**
   * If a try operation return a result, apply the provided mapping function to it, and return and
   * instance of {@link Try} with the applied result.
   *
   * @param mapper a mapping function to apply to the result is available.
   * @param <M> The type of the result of the mapping function
   * @return an instance of {@link Try} describing the result of applying a mapping function to the
   *     result.
   * @throws NullPointerException if the mapping function is null
   * @throws IllegalStateException if a try operation state is {@link Success} but without a result.
   * @throws UnsupportedOperationException if a try operation state is {@link Failure}.
   */
  public abstract <M> Try<M> map(final Function<? super T, ? extends M> mapper);

  /**
   * Return the result if try operation is successful and has a result, otherwise return {@code
   * other} if try operation fails.
   *
   * @param other the value to be returned if there is no result available, it could also be a null.
   * @return the {@code result}, if present, otherwise {@code other}
   * @throws IllegalStateException if a try was successful but returns no result.
   */
  public abstract T orElse(final T other);

  /**
   * Return the result if available after the try operation, otherwise invoke {@code other} and
   * return the result of that invocation.
   *
   * @param other a {@link Supplier} block whose result is returned if try fails
   * @return the try result if available otherwise the result of {@code other.get()}
   * @throws NullPointerException if value is not present and {@code other} is null
   * @throws IllegalStateException if a try was successful but returns no result.
   */
  public abstract T orElseGet(final Supplier<? extends T> other);

  /**
   * Return try result if operation was successful, otherwise throw the exception supplied.
   *
   * @param <X> Type of the exception to be thrown
   * @param exceptionSupplier The supplier which will return the exception to be thrown
   * @return the present value
   * @throws X if there is no value present
   * @throws IllegalStateException if a try was successful but returns no result.
   */
  public abstract <X extends Throwable> T orElseThrow(final Supplier<? extends X> exceptionSupplier)
      throws X;

  /**
   * Use to check the stage of the try operation.
   *
   * @return a {@link Boolean} depending on the state: {@code true} if try was successful else
   *     {@code false} if operation fails.
   */
  public abstract boolean isSuccess();

  /**
   * Use to check the state of a successful try operation if or not it has a result.
   *
   * @return a {@link Boolean} depending on the state: {@code true} if try operation was successful
   *     and has a result or {@code false} if operation fails or successful but without a result.
   */
  public abstract boolean isResult();

  /**
   * Use to check the state of the try operation.
   *
   * @return a {@link Boolean} depending on the state: {@code true} if try operation fails else
   *     {@code false} if operation was successful..
   */
  public abstract boolean isFailure();

  private static class Success<S> extends Try<S> implements Serializable {
    private static final long serialVersionUID = 4332649928027329163L;
    private final boolean isResult;

    private final transient S result;

    private Success() {
      super();
      this.isResult = false;
      this.result = null;
    }

    private Success(final S result) {
      this.isResult = true;
      this.result = result;
    }

    @Override
    public Throwable getCause() {
      throw new UnsupportedOperationException(
          "Operation was successful, without any exception thrown.");
    }

    @Override
    public <M> Try<M> map(final Function<? super S, ? extends M> mapper) {
      Objects.requireNonNull(mapper, "mapper cannot be null");

      if (this.isResult()) {
        return Try.of(() -> mapper.apply(this.result));
      }

      throw new IllegalStateException("No result available to map.");
    }

    @Override
    public S orElse(final S other) {
      return this.get();
    }

    @Override
    public S orElseGet(final Supplier<? extends S> other) {
      return this.get();
    }

    @Override
    public <X extends Throwable> S orElseThrow(final Supplier<? extends X> exceptionSupplier) {
      return this.get();
    }

    @Override
    public S get() {
      if (this.isResult) {
        return this.result;
      }

      throw new IllegalStateException("Operation has no result available.");
    }

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public boolean isResult() {
      return this.isResult;
    }

    @Override
    public boolean isFailure() {
      return false;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof Success) {
        final Success<?> success = (Success<?>) o;
        return isResult() == success.isResult() && this.result == success.result;
      } else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.isResult(), this.result);
    }
  }

  private static class Failure<F> extends Try<F> implements Serializable {
    private static final long serialVersionUID = 6137465851350394283L;

    private final transient Throwable exception;

    private Failure(final Throwable exception) {
      this.exception = exception;
    }

    @Override
    public F get() {
      throw new UnsupportedOperationException(
          "No result available, operation failed with an exception.");
    }

    @Override
    public Throwable getCause() {
      return this.exception;
    }

    @Override
    public <M> Try<M> map(final Function<? super F, ? extends M> mapper) {
      throw new UnsupportedOperationException(
          "No result available to map, operation failed with an exception.");
    }

    @Override
    public F orElse(final F other) {
      return other;
    }

    @Override
    public F orElseGet(final Supplier<? extends F> other) {
      return other.get();
    }

    @Override
    public <X extends Throwable> F orElseThrow(final Supplier<? extends X> exceptionSupplier)
        throws X {
      throw exceptionSupplier.get();
    }

    @Override
    public boolean isSuccess() {
      return false;
    }

    @Override
    public boolean isResult() {
      return false;
    }

    @Override
    public boolean isFailure() {
      return true;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof Failure) {
        final Failure<?> failure = (Failure<?>) o;
        return exception.equals(failure.exception);
      } else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return Objects.hash(exception);
    }
  }
}
