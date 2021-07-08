/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2021 Honerfor, Inc. All Rights Reserved.
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
package security;

import static com.honerfor.cutils.security.AES.Algorithm;
import static com.honerfor.cutils.security.AES.Algorithm.MD2;
import static com.honerfor.cutils.security.AES.Algorithm.MD5;
import static com.honerfor.cutils.security.AES.Algorithm.SHA1;
import static com.honerfor.cutils.security.AES.Algorithm.SHA224;
import static com.honerfor.cutils.security.AES.Algorithm.SHA256;
import static com.honerfor.cutils.security.AES.Algorithm.SHA384;
import static com.honerfor.cutils.security.AES.Algorithm.SHA512;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.honerfor.cutils.security.AES;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.crypto.AEADBadTagException;
import javax.crypto.NoSuchPaddingException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test AES Encryption and Decryption operation.")
final class AesTest {

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  private static class PersonExample implements Serializable {
    private static final long serialVersionUID = -4359123926347587815L;

    private int age;
    private String name;
  }

  private static Stream<Arguments> customObjectResource() {
    final PersonExample personExampleI =
        new PersonExample() {
          {
            setAge(10);
            setName("B0B");
          }
        };

    final PersonExample personExampleII =
        new PersonExample() {
          {
            setAge(11);
            setName("PETER");
          }
        };

    return Stream.of(
        of(personExampleI, "P37s0n3x4mpl3-Cust0m-k3y"),
        of(personExampleII, "n3w P37s0n3x4mpl3-Cust0m-k3y"));
  }

  private static Stream<Arguments> illegalValuesResource() {
    return Stream.of(of(""), of((Object) null));
  }

  private static Stream<Arguments> illegalKeyValuesResource() {
    return Stream.of(of("Should fail", "s0m3 K3y"), of("Should Also fail", "4n0th37 k3Y"));
  }

  private static Stream<Arguments> stringEncryptionValues() {
    return Stream.of(of("Encryption Test", "Xyx", MD5), of("GCM Encryption test", "A K3y", SHA1));
  }

  private static Stream<Arguments> algorithmTypes() {
    return Stream.of(of(MD2), of(MD5), of(SHA1), of(SHA224), of(SHA256), of(SHA384), of(SHA512));
  }

  @DisplayName("Should Throw IllegalArgumentException when trying to encrypt Null values.")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("illegalValuesResource")
  void throwIllegalArgumentExceptionOnEncryption(final Object value) {
    assertThrows(IllegalArgumentException.class, () -> AES.init().encrypt(value));
  }

  @DisplayName("Should Throw IllegalArgumentException when trying to decrypt Null values.")
  @ParameterizedTest(name = "{index} => value={0}")
  @MethodSource("illegalValuesResource")
  void throwIllegalArgumentExceptionOnDecryption(final String value) {
    assertThrows(IllegalArgumentException.class, () -> AES.init().decrypt(value));
  }

  private static Stream<Arguments> intEncryptionValues() {
    return Stream.of(of("10020", "K3y"), of("1929", "K37"), of("-199", "620w37"));
  }

  private static Stream<Arguments> doubleEncryptionValues() {
    return Stream.of(of("10.020", "l0.p3zz"), of("192.99", "l0p3zz"), of("-1.99", "0p3zz"));
  }

  @DisplayName("Should successfully Encrypt and Decrypt Custom Object with Custom Encryption Key")
  @ParameterizedTest(name = "{index} => input={0}, Key={1}")
  @MethodSource("customObjectResource")
  void encryptionAndDecryptionCustomObjectWithCustomKey(final PersonExample input, final String key)
      throws Exception {
    final AES<PersonExample> aes = AES.init(key);
    final String encryptedPersonExample = aes.encrypt(input);
    final PersonExample decryptedPersonExample = aes.decrypt(encryptedPersonExample);

    assertEquals(decryptedPersonExample.age, input.age);
    assertEquals(decryptedPersonExample.name, input.name);
  }

  @DisplayName("Should successfully Encrypt and Decrypt Object with all Algorithm type and Key")
  @ParameterizedTest(name = "{index} => Algorithm={0}")
  @MethodSource("algorithmTypes")
  void encryptionAndDecryptionWithSpecifiedAlgorithmTypeAndCustomKey(final Algorithm algorithm)
      throws Exception {

    final AES<PersonExample> aes = AES.init(algorithm, "Encrypt and Decrypt Key");

    final PersonExample personExample = new PersonExample();
    personExample.setAge(40);
    personExample.setName("Patrick Bet-David");

    final String encryptedPersonExample = aes.encrypt(personExample);
    final PersonExample decryptedPersonExample = aes.decrypt(encryptedPersonExample);

    assertEquals(decryptedPersonExample.age, personExample.age);
    assertEquals(decryptedPersonExample.name, personExample.name);
  }

  @DisplayName("Should successfully Encrypt and Decrypt Object with all Algorithm type")
  @ParameterizedTest(name = "{index} => Algorithm={0}")
  @MethodSource("algorithmTypes")
  void encryptionAndDecryptionWithSpecifiedAlgorithmType(final Algorithm algorithm)
      throws Exception {

    final AES<PersonExample> aes = AES.init(algorithm);

    final PersonExample personExample = new PersonExample();
    personExample.setAge(36);
    personExample.setName("Alex Jones");

    final String encryptedPersonExample = aes.encrypt(personExample);
    final PersonExample decryptedPersonExample = aes.decrypt(encryptedPersonExample);

    assertEquals(decryptedPersonExample.age, personExample.age);
    assertEquals(decryptedPersonExample.name, personExample.name);
  }

  @DisplayName("Should Throw AEADBadTagException when trying to decrypt with wrong Key.")
  @ParameterizedTest(name = "{index} => input={0}, Key={1}")
  @MethodSource("illegalKeyValuesResource")
  void shouldThrowAeadBadTagExceptionOnDecryption(final String input, final String key) {
    assertThrows(
        AEADBadTagException.class,
        () -> {
          final String encryptValue = AES.init(key).encrypt(input);
          AES.<String>init().decrypt(encryptValue);
        });
  }

  @DisplayName("Should successfully encrypt String type values With Default Key.")
  @ParameterizedTest(name = "{index} => input={0}")
  @MethodSource("stringEncryptionValues")
  void shouldEncryptStringTypeValues(final String input) throws Exception {
    final String encryptValue = AES.init().encrypt(input);
    final String decryptedValue = AES.<String>init().decrypt(encryptValue);

    assertEquals(decryptedValue, input);
  }

  @DisplayName("Should successfully encrypt String type values With Custom Key.")
  @ParameterizedTest(name = "{index} => input={0}, Key={0}")
  @MethodSource("stringEncryptionValues")
  void shouldEncryptStringTypeValuesWithCustomKey(final String input, final String key)
      throws Exception {
    final String encryptValue = AES.init(key).encrypt(input);
    final String decryptedValue = AES.<String>init(key).decrypt(encryptValue);

    assertTrue(decryptedValue.equalsIgnoreCase(input));
  }

  @DisplayName("Should successfully encrypt String type values With Custom Key and Algorithm Type.")
  @ParameterizedTest(name = "{index} => input={0}, Key={1}, Algorithm={2}")
  @MethodSource("stringEncryptionValues")
  void shouldEncryptStringTypeValuesWithCustomKeyAndAlgorithmType(
      final String input, final String key, final Algorithm algorithm) throws Exception {
    final String encryptValue = AES.init(algorithm, key).encrypt(input);
    final String decryptedValue = AES.<String>init(algorithm, key).decrypt(encryptValue);

    assertTrue(decryptedValue.equalsIgnoreCase(input));
  }

  @DisplayName("Should successfully encrypt Integers type values With Default Key.")
  @ParameterizedTest(name = "{index} => input={0}")
  @MethodSource("intEncryptionValues")
  void shouldEncryptIntTypeValues(final int input) throws Exception {
    final String encryptValue = AES.init().encrypt(input);
    final int decryptedValue = AES.<Integer>init().decrypt(encryptValue);

    assertEquals(input, decryptedValue);
  }

  @DisplayName("Should successfully encrypt Integers type values With Custom Key.")
  @ParameterizedTest(name = "{index} => input={0}, Key={1}")
  @MethodSource("intEncryptionValues")
  void shouldEncryptIntTypeValuesWithCustomKey(final int input, final String key) throws Exception {
    final String encryptValue = AES.init(key).encrypt(input);
    final int decryptedValue = AES.<Integer>init(key).decrypt(encryptValue);

    assertEquals(input, decryptedValue);
  }

  @DisplayName("Should successfully encrypt Double type values With Default Key.")
  @ParameterizedTest(name = "{index} => input={0}")
  @MethodSource("doubleEncryptionValues")
  void shouldEncryptDoubleTypeValues(final double input) throws Exception {
    final String encryptValue = AES.init().encrypt(input);
    final double decryptedValue = AES.<Double>init().decrypt(encryptValue);

    assertEquals(input, decryptedValue);
  }

  @DisplayName("Should successfully encrypt Double type values With Custom Key.")
  @ParameterizedTest(name = "{index} => input={0}, Key={1}")
  @MethodSource("doubleEncryptionValues")
  void shouldEncryptDoubleTypeValuesWithCustomKey(double input, String key) throws Exception {
    final String encryptValue = AES.init(key).encrypt(input);
    final double decryptedValue = AES.<Double>init(key).decrypt(encryptValue);

    assertEquals(input, decryptedValue);
  }

  @Test
  void equalsAndHashCodeContractToBeValid()
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    final AES<?> aes1 = AES.init("K3¥");
    final AES<?> aes2 = AES.init("K0¥");

    assertEquals(aes1, aes1);
    assertEquals(aes2, aes2);

    assertEquals(aes1.hashCode(), aes1.hashCode());
    assertEquals(aes2.hashCode(), aes2.hashCode());
  }

  @Test
  void equalsAndHashCodeContractToBeInvalid()
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    final AES<?> aes1 = AES.init();
    final AES<?> aes2 = AES.init();

    assertNotEquals(aes1, aes2);
    assertNotEquals(aes1, new ArrayList<>());
    assertNotEquals(aes1.hashCode(), aes2.hashCode());
  }
}
