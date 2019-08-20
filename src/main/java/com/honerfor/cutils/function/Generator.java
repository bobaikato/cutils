package com.honerfor.cutils.function;

/**
 * <p>
 * Represent operations that will return unique/generated values
 * every time {@link Generator#generate()} is called.
 * </p>
 *
 * @param <T>
 * @author B0BAI
 * @since 2.0
 */
@FunctionalInterface
public interface Generator<T> {

    /**
     * <p>Generates values</p>
     *
     * @return return generate values of T type.
     */
    T generate();
}
