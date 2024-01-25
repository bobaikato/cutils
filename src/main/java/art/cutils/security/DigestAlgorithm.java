/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2024 Bobai Kato. All Rights Reserved.
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

import java.security.MessageDigest;
import org.jetbrains.annotations.Contract;

/** The DigestAlgorithm enum represents various digest algorithm types for {@link MessageDigest}. */
public enum DigestAlgorithm {
  /** The MD2 enum represents the MD2 digest algorithm type for {@link MessageDigest}. */
  MD2("MD2"),

  /**
   * The MD5 class represents the MD5 digest algorithm type for MessageDigest. MD5 is a widely used
   * cryptographic hash function that produces a 128-bit (16-byte) hash value. It is commonly used
   * to check the integrity of data and to verify the identity of a message sender.
   */
  MD5("MD5"),

  /**
   * The SHA1 variable represents the SHA-1 digest algorithm type for MessageDigest. SHA-1 is a
   * cryptographic hash function that produces a 160-bit (20-byte) hash value. It is commonly used
   * for data integrity checks and digital signatures.
   */
  SHA1("SHA-1"),

  /**
   * Enum representing the SHA-224 digest algorithm type for MessageDigest. SHA-224 is a
   * cryptographic hash function that produces a 224-bit (28-byte) hash value. It is commonly used
   * for data integrity checks and digital signatures.
   */
  SHA224("SHA-224"),

  /**
   * The SHA256 class represents the SHA-256 digest algorithm type for MessageDigest. SHA-256 is a
   * cryptographic hash function that produces a 256-bit (32-byte) hash value. It is commonly used
   * for data integrity checks and digital signatures.
   */
  SHA256("SHA-256"),

  /**
   * The SHA384 enum represents the SHA-384 digest algorithm type for MessageDigest. SHA-384 is a
   * cryptographic hash function that produces a 384-bit (48-byte) hash value. It is commonly used
   * for data integrity checks and digital signatures.
   */
  SHA384("SHA-384"),

  /**
   * The SHA512 variable represents the SHA-512 digest algorithm type for MessageDigest. SHA-512 is
   * a cryptographic hash function that produces a 512-bit (64-byte) hash value. It is commonly used
   * for data integrity checks and digital signatures.
   */
  SHA512("SHA-512");

  /** The type variable represents the type of the object. */
  private final String type;

  /**
   * The DigestAlgorithm class represents various digest algorithm types for {@link MessageDigest}.
   *
   * <p>The supported digest algorithm types are:
   *
   * <p>- MD2: MD2 is a cryptographic hash function that produces a 128-bit (16-byte) hash value.
   *
   * <p>- MD5: MD5 is a widely used cryptographic hash function that produces a 128-bit (16-byte)
   * hash value.
   *
   * <p>- SHA1: SHA-1 is a cryptographic hash function that produces a 160-bit (20-byte) hash value.
   *
   * <p>- SHA224: SHA-224 is a cryptographic hash function that produces a 224-bit (28-byte) hash
   * value.
   *
   * <p>- SHA256: SHA-256 is a cryptographic hash function that produces a 256-bit (32-byte) hash
   * value.
   *
   * <p>- SHA384: SHA-384 is a cryptographic hash function that produces a 384-bit (48-byte) hash
   * value.
   *
   * <p>- SHA512: SHA-512 is a cryptographic hash function that produces a 512-bit (64-byte) hash
   * value.
   *
   * <p>The DigestAlgorithm instance is created by providing a string representing the type of the
   * digest algorithm.
   *
   * <p>Example usage: DigestAlgorithm md5 = new DigestAlgorithm("MD5");
   *
   * @param type The type of the digest algorithm
   */
  @Contract(pure = true)
  DigestAlgorithm(final String type) {
    this.type = type;
  }

  /**
   * Returns the type of the object.
   *
   * @return The type of the object
   */
  @Contract(pure = true)
  public String getType() {
    return type;
  }
}
