/*
 * Copyright (C) 2018 — 2019 Honerfor, Inc. All Rights Reserved.
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

package com.honerfor.cutils.security;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.Validate.isTrue;

import com.honerfor.cutils.Serialization;
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
import java.util.Objects;
import java.util.function.Supplier;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.SerializationUtils;

/**
 * This is an implementation of Advanced Encryption Standard, to can encrypt and decrypt Objects of
 * any type.
 *
 * @param <T> Type of value
 * @author B0BAI
 * @since 1.0
 */
public class AES<T> {

  /**
   * Instance of {@link Cipher}.
   *
   * @since 1.0
   */
  private final Cipher cipher;

  /**
   * Instance of {@link SecretKeySpec}.
   *
   * @since 1.0
   */
  private final SecretKeySpec secretKey;

  /** GCM Length. */
  private static final int GCM_IV_LENGTH = 12;

  /**
   * Additional Authentication Data for GCM.
   *
   * @since 4.0
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
  private AES(final String encryptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.cipher = Cipher.getInstance("AES/GCM/NoPadding");
    byte[] key = this.aad = encryptionKey.getBytes(StandardCharsets.UTF_8);
    final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    key = Arrays.copyOf(messageDigest.digest(key), 16);
    this.secretKey = new SecretKeySpec(key, "AES");
  }

  /**
   * This initiates encryption.
   *
   * @param <T> Type of value
   * @return Instance of {@link AES}
   * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic
   *     algorithm is requested but is not available in the environment.
   * @throws NoSuchPaddingException This exception is thrown when a particular padding mechanism is
   *     requested but is not available in the environment.
   * @since 1.0
   */
  public static <T> AES<T> init() throws NoSuchAlgorithmException, NoSuchPaddingException {
    final String key = "Set yours with: `AES.init('fW&yNtP2peBndT5Hz&')`";
    return AES.init(key);
  }

  /**
   * This initiates encryption.
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
  public static <T> AES<T> init(final String encryptionKey)
      throws NoSuchAlgorithmException, NoSuchPaddingException {
    return new AES<>(encryptionKey);
  }

  /**
   * This encrypt item of T type.
   *
   * @param itemToEncrypt item to encrypt.
   * @return encrypted string of {@code itemToEncrypt} of T type. Not {@literal null}
   * @throws Exception instance of any exception thrown
   * @since 1.0
   */
  public String encrypt(@Valid final T itemToEncrypt) throws Exception {
    isTrue(isNotEmpty(itemToEncrypt), "Item to encrypt cannot be null.", itemToEncrypt);

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

    if (Objects.nonNull(this.aad)) {
      this.cipher.updateAAD(this.aad);
    }

    final byte[] serializeData = Serialization.serialize(itemToEncrypt);
    byte[] cipherText = this.cipher.doFinal(serializeData);

    final ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
    byteBuffer.put(iv);
    byteBuffer.put(cipherText);

    return Base64.getEncoder().encodeToString(byteBuffer.array());
  }

  /**
   * This method will decrypt the {@code itemToDecrypt}.
   *
   * @param itemToDecrypt encrypted string to be decrypted. not {@literal null}
   * @return decrypted Object.
   * @throws Exception instance of {@link InvalidKeyException}, {@link BadPaddingException} or any
   *     other exception thrown.
   * @since 1.0
   */
  public T decrypt(@NotNull final String itemToDecrypt)
      throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
          IllegalBlockSizeException {

    isTrue(isNotEmpty(itemToDecrypt), "Item to decrypt cannot be null.", itemToDecrypt);

    final byte[] cipherMessage = Base64.getDecoder().decode(itemToDecrypt);
    final AlgorithmParameterSpec spec = new GCMParameterSpec(128, cipherMessage, 0, GCM_IV_LENGTH);

    this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey, spec);

    if (Objects.nonNull(this.aad)) {
      this.cipher.updateAAD(this.aad);
    }

    final byte[] plainText =
        this.cipher.doFinal(cipherMessage, GCM_IV_LENGTH, cipherMessage.length - GCM_IV_LENGTH);

    return SerializationUtils.deserialize(plainText);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AES) {
      final AES<?> aes = (AES<?>) o;
      if (cipher.equals(aes.cipher) && secretKey.equals(aes.secretKey)) {
        return Arrays.equals(aad, aes.aad);
      }
      return false;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(cipher, secretKey);
    result = 31 * result + Arrays.hashCode(aad);
    return result;
  }
}
