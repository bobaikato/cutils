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

package art.cutils.security;

import art.cutils.Serialization;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Supplier;

import static art.cutils.security.AES.Algorithm.SHA256;
import static java.security.MessageDigest.getInstance;
import static java.util.Objects.hash;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.Validate.isTrue;

/**
 * This is an implementation of Advanced Encryption Standard, to can encrypt and decrypt Objects of
 * any type.
 *
 * @param <T> Type of value
 * @author @author <a href="https://github.com/B0BAI">Bobai Kato</a>
 * @since 1.0
 */
public final class AES<T> implements Serializable {
  private static final long serialVersionUID = 977987773346721438L;
  /**
   * Default encryption Key.
   *
   * @since 1.0
   */
  private static final String DEFAULT_KEY = "Set yours with: `AES.init('fW&yNtP2peBndT5Hz&')`";
  /** GCM Length. */
  private static final int GCM_IV_LENGTH = 12;
  /**
   * Instance of {@link Cipher}.
   *
   * @since 1.0
   */
  private final transient Cipher cipher;

  /**
   * Instance of {@link SecretKeySpec}.
   *
   * @since 1.0
   */
  private final SecretKeySpec secretKey;
  /**
   * Additional Authentication Data for GCM.
   *
   * @since 1.0
   */
  private final byte[] aad;

  /**
   * Default Constructor.
   *
   * @param encryptionKey meta data you want to verify secret message
   * @throws NoSuchPaddingException when a bad/Wrong encryption key is supplied.
   * @throws NoSuchAlgorithmException This exception is thrown when a cryptographic algorithm not
   *     available in the environment.
   * @since 1.0
   */
  private AES(final AES.@NotNull Algorithm algorithm, final @NotNull String encryptionKey)
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.cipher = Cipher.getInstance("AES/GCM/NoPadding");
    byte[] key = this.aad = encryptionKey.getBytes(StandardCharsets.UTF_8);
    final MessageDigest messageDigest = getInstance(algorithm.type);
    key = Arrays.copyOf(messageDigest.digest(key), 16);
    this.secretKey = new SecretKeySpec(key, "AES");
  }

  /**
   * This initiates encryption with default {@link MessageDigest} {@link Algorithm#type} and
   * encryption key.
   *
   * @param <T> Type of value
   * @return Instance of {@link AES}
   * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic
   *     algorithm is requested but is not available in the environment.
   * @throws NoSuchPaddingException This exception is thrown when a particular padding mechanism is
   *     requested but is not available in the environment.
   * @since 1.0
   */
  @Contract(" -> new")
  public static <T> @NotNull AES<T> init() throws NoSuchAlgorithmException, NoSuchPaddingException {
    return AES.init(AES.DEFAULT_KEY);
  }

  /**
   * This initiates encryption with the default {@link MessageDigest} {@link Algorithm#type}:{@code
   * SHA-256}.
   *
   * @param encryptionKey user encryption Key
   * @param <T> Type of value
   * @return Instance of {@link AES}
   * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic
   *     algorithm is requested but is not available in the environment.
   * @throws NoSuchPaddingException This exception is thrown when a particular padding mechanism is
   *     requested but is not available in the environment.
   * @since 1.0
   */
  @Contract("_ -> new")
  public static <T> @NotNull AES<T> init(final String encryptionKey)
      throws NoSuchAlgorithmException, NoSuchPaddingException {
    requireNonNull(encryptionKey, "encryption Key cannot be null");
    return new AES<>(SHA256, encryptionKey);
  }

  /**
   * This initiates encryption with the specified {@link MessageDigest} {@link Algorithm#type} type
   * and default Key.
   *
   * @param algorithm encryption algorithm of {@link Algorithm} instance. SHA256 is set if {@code
   *     null}.
   * @param <T> Type of value
   * @return Instance of {@link AES}
   * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic
   *     algorithm is requested but is not available in the environment.
   * @throws NoSuchPaddingException This exception is thrown when a particular padding mechanism is
   *     requested but is not available in the environment.
   * @since 1.0
   */
  @Contract("_ -> new")
  public static <T> @NotNull AES<T> init(final AES.Algorithm algorithm)
      throws NoSuchAlgorithmException, NoSuchPaddingException {
    return AES.init(algorithm, AES.DEFAULT_KEY);
  }

  /**
   * This initiates encryption with the specified {@link MessageDigest} {@link Algorithm#type} and
   * encryption key.
   *
   * @param encryptionKey user encryption Key
   * @param algorithm encryption algorithm of {@link Algorithm} instance. SHA256 is set if {@code
   *     null}.
   * @param <T> Type of value
   * @return Instance of {@link AES}
   * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic
   *     algorithm is requested but is not available in the environment.
   * @throws NoSuchPaddingException This exception is thrown when a particular padding mechanism is
   *     requested but is not available in the environment.
   * @since 1.0
   */
  @Contract("_, _ -> new")
  public static <T> @NotNull AES<T> init(final AES.Algorithm algorithm, final String encryptionKey)
      throws NoSuchAlgorithmException, NoSuchPaddingException {
    requireNonNull(encryptionKey, "encryption Key cannot be null");
    return new AES<>(isNull(algorithm) ? SHA256 : algorithm, encryptionKey);
  }

  /**
   * This encrypt item of T type.
   *
   * @param itemToEncrypt item to encrypt.
   * @return encrypted string of {@code itemToEncrypt} of T type. Not {@literal null}
   * @throws InvalidAlgorithmParameterException This is the exception for invalid or inappropriate
   *     algorithm parameters.
   * @throws InvalidKeyException This is the exception for invalid Keys (invalid encoding, wrong *
   *     length, uninitialized, etc).
   * @throws BadPaddingException This exception is thrown when a particular padding mechanism is
   *     expected for the input data but the data is not padded properly.
   * @throws IllegalBlockSizeException This exception is thrown when the length of data provided to
   *     a block cipher is incorrect, i.e., does not match the block size of the cipher.
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   */
  public String encrypt(@Valid final T itemToEncrypt)
      throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
          IllegalBlockSizeException, IOException {
    Validate.isTrue(isNotEmpty(itemToEncrypt), "Item to encrypt cannot be null.", itemToEncrypt);
    final Supplier<byte[]> ivSupplier =
        () -> {
          final SecureRandom secureRandom = new SecureRandom();
          final byte[] iv = new byte[GCM_IV_LENGTH]; // NEVER REUSE THIS IV WITH SAME KEY
          do {
            secureRandom.nextBytes(iv);
          } while (iv[0] == 0);
          return iv;
        };

    final byte[] iv = ivSupplier.get();

    final GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
    this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, parameterSpec);

    if (nonNull(this.aad)) {
      this.cipher.updateAAD(this.aad);
    }

    final byte[] serializeData = Serialization.serialize(itemToEncrypt);
    final byte[] cipherText = this.cipher.doFinal(serializeData);

    final ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
    byteBuffer.put(iv);
    byteBuffer.put(cipherText);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
  }

  /**
   * This method will decrypt the {@code itemToDecrypt}.
   *
   * @param itemToDecrypt encrypted string to be decrypted. not {@literal null}
   * @return decrypted Object.
   * @throws InvalidAlgorithmParameterException exception for invalid or inappropriate algorithm
   *     parameters.
   * @throws InvalidKeyException exception for invalid Keys
   * @throws BadPaddingException This exception is thrown when a particular padding mechanism is *
   *     expected for the input data but the data is not padded properly.
   * @throws IllegalBlockSizeException This exception is thrown when the length of data provided to
   *     a block * cipher is incorrect, i.e., does not match the block size of the cipher.
   */
  public T decrypt(final @NotNull String itemToDecrypt)
      throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
          IllegalBlockSizeException {

    isTrue(isNotEmpty(itemToDecrypt), "Item to decrypt cannot be null.", itemToDecrypt);

    final byte[] cipherMessage = Base64.getUrlDecoder().decode(itemToDecrypt);
    final AlgorithmParameterSpec spec = new GCMParameterSpec(128, cipherMessage, 0, GCM_IV_LENGTH);

    this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey, spec);

    if (nonNull(this.aad)) {
      this.cipher.updateAAD(this.aad);
    }

    final byte[] plainText =
        this.cipher.doFinal(cipherMessage, GCM_IV_LENGTH, cipherMessage.length - GCM_IV_LENGTH);

    return SerializationUtils.deserialize(plainText);
  }

  @Override
  public int hashCode() {
    int result = hash(this.cipher, this.secretKey);
    result = 31 * result + Arrays.hashCode(this.aad);
    return result;
  }

  @Override
  @Contract(value = "null -> false", pure = true)
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AES) {
      final AES<?> aes = (AES<?>) o;
      if (this.cipher.equals(aes.cipher) && this.secretKey.equals(aes.secretKey)) {
        return Arrays.equals(this.aad, aes.aad);
      }
      return false;
    }
    return false;
  }

  /**
   * This holds valid {@link MessageDigest} algorithm types.
   *
   * @since 1.0
   */
  public enum Algorithm {
    /** MD2 algorithm type. */
    MD2("MD2"),

    /** MD5 algorithm type. */
    MD5("MD5"),

    /** SHA-1 algorithm type. */
    SHA1("SHA-1"),

    /** SHA-224 algorithm type. */
    SHA224("SHA-224"),

    /** SHA-256 algorithm type. */
    SHA256("SHA-256"),

    /** SHA-384 algorithm type. */
    SHA384("SHA-384"),

    /** SHA-512 algorithm type. */
    SHA512("SHA-512");

    private final String type;

    @Contract(pure = true)
    Algorithm(final String type) {
      this.type = type;
    }
  }
}
