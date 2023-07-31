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

package art.cutils.value;

import art.cutils.function.Accepter;
import art.cutils.function.Dealer;
import art.cutils.function.Executable;
import art.cutils.function.ThrowingFunction;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
 * which has not resulted will throw an {@link IllegalStateException}, using the {@link
 * Try#map(ThrowingFunction)} will vary depending on the state of the operation ... and so on.
 * Review the method description for more information.
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
 * @author <a href="https://github.com/bobaikato">Bobai Kato</a>
 * @since v1
 */
public abstract class Try<T> implements Serializable {
  private static final long serialVersionUID = 4530258067856316628L;

  /** Locked for life. */
  @Contract(pure = true)
  private Try() {}

  /**
   * Accepts a {@link Dealer} type function which is expected to return a result if operation was
   * successful.
   *
   * @param operation the operation that will be tried, a variable of {@link Dealer} type.
   * @param <T> variable type
   * @return instance of {@link Try} either with a {@link Success} or {@link Failure} state.
   * @since v1
   */
  @Contract("_ -> new")
  public static <T> @NotNull Try<T> of(final Dealer<? extends T> operation) {
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
   * @since v1
   */
  @Contract("_ -> new")
  public static <T> @NotNull Try<T> of(final Executable operation) {
    Objects.requireNonNull(operation, "operation cannot be null");
    try {
      operation.execute();
      return new Success<>();
    } catch (final Exception e) {
      return new Failure<>(e);
    }
  }

  /**
   * If {@link Try#isSuccess()}, {@link Try#isResult()} and {@link Try#isNotEmpty()} ()} invoke the
   * specified consumer with the operation result, otherwise do nothing.
   *
   * @param result block of operation to be executed.
   * @see Success#get()
   * @see Try#isSuccess()
   * @see Try#isResult()
   * @see Try#isNotEmpty()
   * @since v1
   * @return existing instance of {@link Try}
   */
  public Try<T> onSuccess(final Consumer<? super T> result) {
    Objects.requireNonNull(result, "Success result Consumer cannot be null.");
    if (this.isSuccess() && this.isResult() && this.isNotEmpty()) {
      result.accept(this.get());
    }
    return this;
  }

  /**
   * Use to check the stage of the try operation.
   *
   * @return a {@link Boolean} depending on the state: {@code true} if try was successful else
   *     {@code false} if operation fails.
   */
  public abstract boolean isSuccess();

  /**
   * Try is Empty if operation is Failed or Filter condition is not met.
   *
   * @return a {@link Boolean} depending on the state: {@code true} if try is empty else {@code
   *     false}
   * @see Try#filter(Predicate)
   * @see Try#isFailure()
   * @see Try#isSuccess()
   * @see Try#isNotEmpty()
   * @since v2.4
   */
  public abstract boolean isEmpty();

  /**
   * If try filter condition is not met, invoke the specified {@link Runnable}.
   *
   * @implNote this method is not intended to be used for handling failed operations. Use {@link
   *     Try#onFailure(Runnable)} to handle failed operation.
   * @implSpec Try will invoke {@link Try#onEmpty(Runnable)} only if filter condition is not met.
   * @param run the operation to be executed if filter condition is not met.
   * @return existing instance of {@link Try}
   * @since v2.4.5
   * @see Try#onFailure(Runnable)
   * @see Try#filter(Predicate)
   */
  public abstract Try<T> onEmpty(final Runnable run);

  /**
   * Try is Not Empty if operation is Successful or filter condition is met.
   *
   * @implSpec default implementation of {@link Try#isNotEmpty()} is {@code !isEmpty()}
   * @return a {@link Boolean} depending on the state: {@code true} if try is not empty else {@code
   *     false}
   * @see Try#filter(Predicate)
   * @see Try#isFailure()
   * @see Try#isSuccess()
   * @see Try#isEmpty()
   * @since v2.4
   */
  public boolean isNotEmpty() {
    return !this.isEmpty();
  }

  /**
   * Use this method to retrieve the try operation result.
   *
   * @return the try operation result
   * @throws IllegalStateException Try state is {@link Success} without an available result.
   * @throws UnsupportedOperationException Try state is {@link Failure} when a try operation fails
   * @since v1
   */
  public abstract T get();

  /**
   * If a result is present, and the result matches the given predicate, returns a {@code Try}
   * describing the result, otherwise returns an empty {@link Try}
   *
   * @param predicate the predicate to apply to a result, if present
   * @return an {@link Try} describing the value of this {@link Try}, if a result is present and the
   *     result matches the given predicate, otherwise an empty {@link Try}
   * @throws NullPointerException if the predicate is {@code null}
   * @since v2.4
   */
  public abstract Try<T> filter(final Predicate<? super T> predicate);

  /**
   * Safely, performing an action on the current try result if present. Unlike {@link
   * Try#map(ThrowingFunction)} this method will not affect the current {@link Try} results.
   *
   * <p>The same result will be returned with the {@link Try} instance.
   *
   * @implSpec Use this method instead {@link Try#onSuccess(Runnable)} to safely perform an action
   *     on the result.
   * @implNote if the current try is a {@link Failure} it will return a new {@link Failure} instance
   * @param acceptor the action to be performed on the result if present.
   * @return a {@link Try} consisting of the Result, if a result is present, otherwise an empty
   */
  public abstract Try<T> peek(final Accepter<? super T> acceptor);

  /**
   * Use this method to retrieve the try operation {@link Optional} result.
   *
   * @return try operation {@link Optional} result.
   * @throws IllegalStateException Try state is {@link Success} without an available result.
   * @throws UnsupportedOperationException Try state is {@link Failure} when a try operation fails
   * @since v1
   */
  public abstract Optional<T> getOptional();

  /**
   * If {@link Try#isSuccess()} and {@link Try#isNotEmpty()}, invoke the specified {@link Runnable}.
   *
   * @param runnable the {@link Runnable} to be executed
   * @return existing instance of {@link Try}
   * @since v1
   * @see Try#isNotEmpty()
   * @see Try#isSuccess()
   */
  public Try<T> onSuccess(final Runnable runnable) {
    Objects.requireNonNull(runnable, "Success Runnable cannot be null.");
    if (this.isSuccess() && this.isNotEmpty()) {
      runnable.run();
    }
    return this;
  }

  /**
   * If try operations fails, invoke the specified consumer with the exception thrown during the
   * execution, otherwise do nothing.
   *
   * @param cause the consumer to accept the cause and executed if an exception was thrown
   * @see Failure#getCause()
   * @since v1
   */
  public Try<T> onFailure(final Consumer<? super Throwable> cause) {
    Objects.requireNonNull(cause, "Failure cause Consumer cannot be null.");
    if (this.isFailure()) {
      cause.accept(this.getCause());
    }
    return this;
  }

  /**
   * Use to check the state of the try operation.
   *
   * @return a {@link Boolean} depending on the state: {@code true} if try operation fails else
   *     {@code false} if operation was successful..
   */
  public abstract boolean isFailure();

  /**
   * Retrieve the Cause of try operation failure.
   *
   * @return exception thrown during try operation.
   * @throws UnsupportedOperationException if try operation is successful
   * @since v1
   */
  public abstract Throwable getCause();

  /**
   * If try operations fails, invoke the specified {@link Runnable}.
   *
   * @param run the {@link Runnable} to be executed
   * @return existing instance of {@link Try}
   * @since v2
   */
  public Try<T> onFailure(final Runnable run) {
    Objects.requireNonNull(run, "Failure Runnable cannot be null.");
    if (this.isFailure()) {
      run.run();
    }
    return this;
  }

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
  public abstract <M> Try<M> map(final ThrowingFunction<? super T, ? extends M> mapper);

  /**
   * Return the result if try operation is successful and has a result, otherwise return {@code
   * other} value.
   *
   * @param other the value to be returned if there is no result available, it could also be a null.
   * @return the {@code result}, if present, otherwise {@code other}
   * @throws IllegalStateException if a try was successful but returns no result.
   */
  public abstract T orElseGet(final T other);

  /**
   * Return the result if available after the try operation, otherwise invoke supply {@code other}
   * result
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
   * Return try result if operation was successful, otherwise throw the exception supplied. * @param
   * throwable The {@link Throwable} which will to be thrown
   *
   * @return the present value
   * @throws IllegalStateException if a try was successful but returns no result.
   */
  public abstract T orElseThrow(final Throwable throwable);

  /**
   * Use to check the state of a successful try operation whether or not it has a result.
   *
   * @return a {@link Boolean} depending on the state: {@code true} if try operation was successful
   *     and has a result or {@code false} if operation fails or successful but without a result.
   */
  public abstract boolean isResult();

  private static class Success<S> extends Try<S> implements Serializable {
    private static final long serialVersionUID = 4332649928027329163L;
    private boolean isResult;
    private S result;

    private boolean empty;

    private Success(final boolean empty) {
      this();
      this.empty = empty;
    }

    private Success(final S result) {
      this.isResult = true;
      this.result = result;
    }

    private Success() {
      super();
      this.isResult = false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return Objects.hash(this.isResult(), this.result);
    }

    /** {@inheritDoc} */
    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof Success) {
        final Success<?> success = (Success<?>) o;
        return this.isResult() == success.isResult()
            && this.result == success.result
            && this.empty == success.empty;
      } else {
        return false;
      }
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isSuccess() {
      return true;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isEmpty() {
      return this.empty;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable Try<S> onEmpty(final Runnable task) {
      Objects.requireNonNull(task, "on Empty Runnable cannot be null.");
      if (this.isEmpty()) {
        task.run();
      }
      return this;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isNotEmpty() {
      return !this.empty;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public S get() {
      return this.result;
    }

    /** {@inheritDoc} */
    @Override
    @Contract("_ -> this")
    public Try<S> filter(final Predicate<? super S> predicate) {
      Objects.requireNonNull(predicate, "Filter Predicate cannot be null.");
      if (this.isResult && this.isNotEmpty()) {
        if (predicate.test(this.result)) {
          return this;
        }
        this.clean();
      }
      return this;
    }

    /**
     * Clean the try operation state.
     *
     * @implNote This method is used to clean the try operation state when a condition is not met.
     */
    @Contract(mutates = "this")
    private void clean() {
      this.empty = true; // empty, condition is not met
      this.isResult = false; // no result
      this.result = null; // clear result
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public @Nullable Try<S> peek(final Accepter<? super S> acceptor) {
      Objects.requireNonNull(acceptor, "Accepter cannot be null.");
      if (this.isResult && this.isNotEmpty()) {
        try {
          acceptor.accept(this.result);
        } catch (final Throwable cause) {
          return new Failure<>(cause);
        }
      }
      return this;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public Optional<S> getOptional() {
      return Optional.ofNullable(this.result);
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isFailure() {
      return false;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public @Nullable Throwable getCause() {
      return null;
    }

    /** {@inheritDoc} */
    @Override
    public <M> @NotNull Try<M> map(final ThrowingFunction<? super S, ? extends M> mapper) {
      Objects.requireNonNull(mapper, "Mapper cannot be null.");
      if (this.isResult && this.isNotEmpty()) {
        return Try.of(() -> mapper.apply(this.result));
      }

      return new Success<>(true);
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public S orElseGet(final S other) {
      return this.isResult ? this.result : other;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public S orElseGet(final Supplier<? extends S> other) {
      Objects.requireNonNull(other, "Supplier cannot be null.");
      return this.isResult ? this.result : other.get();
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public <X extends Throwable> S orElseThrow(final Supplier<? extends X> exceptionSupplier) {
      return this.get();
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public S orElseThrow(final Throwable throwable) {
      return this.get();
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isResult() {
      return this.isResult;
    }
  }

  private static class Failure<F> extends Try<F> implements Serializable {
    private static final long serialVersionUID = 6137465851350394283L;

    private final transient Throwable exception;

    private Failure(final Throwable exception) {
      this.exception = exception;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isSuccess() {
      return false;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isEmpty() {
      return true;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(value = " -> this", pure = true)
    public Try<F> onEmpty(final Runnable run) {
      return this;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public @Nullable F get() {
      return null;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(value = "_ -> this", pure = true)
    public Try<F> filter(final Predicate<? super F> predicate) {
      return this;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote No operation is performed because on {@link Failure} no result is present.
     */
    @Override
    @Contract(value = "_ -> this", pure = true)
    public Try<F> peek(final Accepter<? super F> acceptor) {
      return this;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public Optional<F> getOptional() {
      return Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isFailure() {
      return true;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public Throwable getCause() {
      return this.exception;
    }

    /** {@inheritDoc} */
    @Override
    @Contract(value = "_ -> fail", pure = true)
    public <M> @NotNull Try<M> map(final ThrowingFunction<? super F, ? extends M> mapper) {
      return new Failure<>(this.exception);
    }

    /** {@inheritDoc} */
    @Override
    @Contract(value = "_ -> param1", pure = true)
    public F orElseGet(final F other) {
      return other;
    }

    /** {@inheritDoc} */
    @Override
    public F orElseGet(final @NotNull Supplier<? extends F> other) {
      Objects.requireNonNull(other, "Supplier cannot be null.");
      return other.get();
    }

    /** {@inheritDoc} */
    @Override
    @Contract("_ -> fail")
    public <X extends Throwable> F orElseThrow(
        final @NotNull Supplier<? extends X> exceptionSupplier) throws X {
      Objects.requireNonNull(exceptionSupplier, "Exception supplier cannot be null.");
      throw exceptionSupplier.get();
    }

    /** {@inheritDoc} */
    @Override
    @Contract(value = "_ -> fail", pure = true)
    public F orElseThrow(final @NotNull Throwable throwable) {
      try {
        throw throwable;
      } catch (final Throwable ex) {
        throw new RuntimeException(ex);
      }
    }

    /** {@inheritDoc} */
    @Override
    @Contract(pure = true)
    public boolean isResult() {
      return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return Objects.hash(this.exception);
    }

    /** {@inheritDoc} */
    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof Failure) {
        final Failure<?> failure = (Failure<?>) o;
        return this.exception.equals(failure.exception);
      } else {
        return false;
      }
    }
  }
}
