package com.honerfor.cutils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is class is responsible for hatching Random Strings or Alpha-numeric String
 *
 * <p>
 * Example/Usage:
 * <code>
 * <p>
 * StringIncubator gen = new StringIncubator(12);
 * StringIncubator session = new StringIncubator();
 * <p>
 * StringIncubator gen = new StringIncubator(12, ThreadLocalRandom.current());
 * <p>
 * String symbol = StringIncubator.DIGITS + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
 * StringIncubator tickets = new StringIncubator(23, new SecureRandom(), symbol);
 * </code>
 *
 * @author B0BAI
 * @author Erickson (https://stackoverflow.com/users/3474/erickson)
 * @since 2.0
 */
public class StringIncubator {

    private final Random random;

    private final char[] symbols;

    private final char[] buffer;

    /**
     * <p>Upper-case Alphabets</p>
     *
     * @since 2.0
     */
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * <p>Lower-case Alphabets</p>
     *
     * @since 2.0
     */
    private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);

    /**
     * <p>Digits variable</p>
     *
     * @since 2.0
     */
    private static final String DIGITS = "0123456789";

    /**
     * <p>Alpha-numeric variable</p>
     *
     * @since 2.0
     */
    private static final String ALPHANUM = UPPER + LOWER + DIGITS;

    /**
     * <p>
     * Constructor which take the require hatch length, instance of {@link Random} and
     * {@link String} from which to hatch the random values.
     * </P>
     *
     * @param length  of hatched string/value
     * @param random  any instance of {@link Random} as required
     * @param symbols String to generate the hatched values
     * @since 2.0
     */
    public StringIncubator(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = (symbols).toCharArray();
        this.buffer = new char[length];
    }

    /**
     * <p>Constructor to hatch an alphanumeric string generator.</p>
     *
     * @param length of hatched string/value
     * @param random any instance of {@link Random} as required
     * @since 2.0
     */
    public StringIncubator(int length, Random random) {
        this(length, random, ALPHANUM);
    }

    /**
     * <p>Constructor to hatch an alphanumeric string generator.</p>
     *
     * @param length of hatched string/value
     * @param random any instance of {@link ThreadLocalRandom} as required
     * @since 2.0
     */
    public StringIncubator(int length, ThreadLocalRandom random) {
        this(length, random, ALPHANUM);
    }

    /**
     * <p>Create an alphanumeric strings from a secure generator.</p>
     *
     * @param length of hatched string/value
     * @since 2.0
     */
    public StringIncubator(int length) {
        this(length, new SecureRandom());
    }

    /**
     * <p>Create session identifiers</p>
     *
     * @since 2.0
     */
    public StringIncubator() {
        this(64);
    }

    /**
     * <p>Generate and returns random string.</p>
     *
     * @since 2.0
     */
    public String hatch() {
        int bound = buffer.length;
        for (int idx = 0; idx < bound; idx++) {
            buffer[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buffer);
    }
}