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

import com.honerfor.cutils.security.AES;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.crypto.BadPaddingException;
import java.io.Serializable;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Test AES Encryption and Decryption operation.")
final class AESTest {

    private static class PersonExample implements Serializable {
        private static final long serialVersionUID = -4359123926347587815L;

        private int age;
        private String name;

        void setAge(int age) {
            this.age = age;
        }

        void setName(String name) {
            this.name = name;
        }
    }

    @DisplayName("Should successfully Encrypt and Decrypt Custom Object with Custom Encryption Key")
    @ParameterizedTest(name = "{index} => input={0}")
    @MethodSource("customObjectResource")
    void encryptionAndDecryptionCustomObjectWithCustomKey(PersonExample input) throws Exception {
        final AES<PersonExample> aes = AES.init("P37s0n3x4mpl3-Cust0m-k3y");
        final String encryptedPersonExample = aes.encrypt(input);
        final PersonExample decryptedPersonExample = aes.decrypt(encryptedPersonExample);

        assertEquals(decryptedPersonExample.age, input.age);
        assertEquals(decryptedPersonExample.name, input.name);
    }

    @DisplayName("Should successfully Encrypt and Decrypt Custom Object with default Encryption Key")
    @ParameterizedTest(name = "{index} => input={0}")
    @MethodSource("customObjectResource")
    void encryptionAndDecryptionCustomObjectWithDefaultKey(PersonExample input) throws Exception {
        final AES<PersonExample> aes = AES.init();
        final String encryptedPersonExample = aes.encrypt(input);
        final PersonExample decryptedPersonExample = aes.decrypt(encryptedPersonExample);

        assertEquals(decryptedPersonExample.age, input.age);
        assertEquals(decryptedPersonExample.name, input.name);
    }

    private static Stream<Arguments> customObjectResource() {
        final PersonExample personExampleI = new PersonExample() {{
            setAge(10);
            setName("B0B");
        }};

        final PersonExample personExampleII = new PersonExample() {{
            setAge(11);
            setName("PETER");
        }};

        return Stream.of(
                Arguments.of(personExampleI),
                Arguments.of(personExampleII)
        );
    }


    @DisplayName("Should successfully Encrypt Objects with default Encryption Key")
    @ParameterizedTest(name = "{index} => itemToBeEncrypted={0}, encryptedString={1}")
    @MethodSource("defaultKeyEncryptionResource")
    void encryptObjectWithDefaultKey(Object itemToBeEncrypted, String encryptedString) throws Exception {
        assertEquals(AES.init().encrypt(itemToBeEncrypted), encryptedString);
    }

    private static Stream<Arguments> defaultKeyEncryptionResource() {
        return Stream.of(
                Arguments.of("Testing Encryption", "H4t4eUdXKswQTwVueHkP1gOX0wuogYDuoYxzx/ZKX/A="),
                Arguments.of(89.02, "cUrT5eqdnoFAkDBIVSUOQ6fDYnWQ4oBetbvYjnVcLwLE/l56Rt3nnB/hPtLdq4/xB1Kt2tlf3xnFzQGL4fXHG8+3w3eypuUVDJYLRsO5O+itutmNHten5ba1M9/8akaK"),
                Arguments.of(100L, "omzt94M4J4KgvD4bL96b5nRaatd5d/EifjppGVmbcHA1+gUewP82qIiEebKXpWT6I1hJYxfRC52dAEFODiCu3f1keWLMhLusNsosaFKu3tNrYMLDMo7W2Bp6aeAk1hKG"),
                Arguments.of(88, "uMwQNEYSTmbQ5f6gD+pjoy1y8EYL7J1oAt8EQnDYMUwJL40q4cl1l2R7hZ3e0cRQ8g6xqg0iEvgWdubU7wbvoGXlY506qJZ9debfcHERf3uyl/TQ3eBqnqRxvb06maQJ")
        );
    }

    @DisplayName("Should successfully Decrypt Objects with default Encryption Key")
    @ParameterizedTest(name = "{index} => output={0}, itemToBeDecrypted={1}")
    @MethodSource("defaultKeyDecryptionResource")
    void decryptObjectWithDefaultKey(Object output, String itemToBeDecrypted) throws Exception {
        assertEquals(AES.init().decrypt(itemToBeDecrypted), output);
    }

    private static Stream<Arguments> defaultKeyDecryptionResource() {
        return Stream.of(
                Arguments.of("Testing Decryption", "cj/1cfdTniTzIFsuUrpEXjLcjtYNRMPk3Ty2QRqGs8A="),
                Arguments.of(88.02, "cUrT5eqdnoFAkDBIVSUOQ6fDYnWQ4oBetbvYjnVcLwLE/l56Rt3nnB/hPtLdq4/xB1Kt2tlf3xnFzQGL4fXHG/dwvhJqtcX4F5LZRtZU35qtutmNHten5ba1M9/8akaK"),
                Arguments.of(12L, "omzt94M4J4KgvD4bL96b5nRaatd5d/EifjppGVmbcHA1+gUewP82qIiEebKXpWT6I1hJYxfRC52dAEFODiCu3f1keWLMhLusNsosaFKu3tMNEFs6ZdgnATPRNNqhitiW"),
                Arguments.of(100, "uMwQNEYSTmbQ5f6gD+pjoy1y8EYL7J1oAt8EQnDYMUwJL40q4cl1l2R7hZ3e0cRQ8g6xqg0iEvgWdubU7wbvoGXlY506qJZ9debfcHERf3uavmXNdMC7lj1HsjpptQuS")
        );
    }


    @DisplayName("Should successfully Encrypt Objects with Custom Encryption Key")
    @ParameterizedTest(name = "{index} => itemToBeEncrypted={0}, encryptedString={1}")
    @MethodSource("customKeyEncryptionResource")
    void encryptObjectWithCustomKey(Object itemToBeEncrypted, String encryptedString) throws Exception {
        assertEquals(AES.init("My-Custom-Key").encrypt(itemToBeEncrypted), encryptedString);
    }

    private static Stream<Arguments> customKeyEncryptionResource() {
        return Stream.of(
                Arguments.of("Testing Decryption", "s68hIJWxSG09ZQjbGF/6oQ4c2a8wHXnHPbR92wV2PQk="),
                Arguments.of(89.02, "4OkXLhHBDmq54mtV2fp+kTj4xtONwwKNUx6rjppRT0b/E/WueHmSEwGiAYXRPRwQCEEWOcTcW+p1BRNnraepjyagvKE+vb2iUKesV2BwH3j8aFusIpAE7+Ei61R1qdjD"),
                Arguments.of(100L, "sFaXqId3I2kIVEgrLjdRw7pVdlfITHrLJIq3LkkLCTFUR64LJymRedu7Ez+ULbjvq9xZw2Fhei0SXu4O6WCF800jKhHkXp26VPLZOpPwEKwk6XfWZyr/I6IyC7S7DAwJ"),
                Arguments.of(88, "QDj1XtBhN4ejgbKLvqoEFB6wtvEvCfL5TzD69/Kw/NJXubz9WruX2JV8Kmr+QyqPfQ6AoqIq915Do0P3TehkvWBXYSVT2xfFT+wCAUcKlNLXxL1Jfpy+d61abGNw/NYC")
        );
    }

    @DisplayName("Should successfully Decrypt Objects with Custom Encryption Key")
    @ParameterizedTest(name = "{index} => output={0}, itemToBeDecrypted={1}")
    @MethodSource("customKeyOperationResource")
    void decryptObjectWithCustomKey(Object output, String itemToBeDecrypted) throws Exception {
        assertEquals(AES.init("My-Custom-Key").decrypt(itemToBeDecrypted), output);
    }

    @DisplayName("Should Throw BadPaddingException when using a different key to decrypt.")
    @ParameterizedTest(name = "{index} => output={0}, itemToBeDecrypted={1}")
    @MethodSource("customKeyOperationResource")
    void throwBadPaddingExceptionOnDecryption(Object output, String itemToBeDecrypted) {
        assertThrows(BadPaddingException.class, () -> assertEquals(AES.init("The-Wrong-Custom-Encryption-Key").decrypt(itemToBeDecrypted), output));
    }

    private static Stream<Arguments> customKeyOperationResource() {
        return Stream.of(
                Arguments.of("Testing Decryption", "s68hIJWxSG09ZQjbGF/6oQ4c2a8wHXnHPbR92wV2PQk="),
                Arguments.of(88.02, "4OkXLhHBDmq54mtV2fp+kTj4xtONwwKNUx6rjppRT0b/E/WueHmSEwGiAYXRPRwQCEEWOcTcW+p1BRNnraepj9+0m+rBHV6y9rYG23WjWvj8aFusIpAE7+Ei61R1qdjD"),
                Arguments.of(12L, "sFaXqId3I2kIVEgrLjdRw7pVdlfITHrLJIq3LkkLCTFUR64LJymRedu7Ez+ULbjvq9xZw2Fhei0SXu4O6WCF800jKhHkXp26VPLZOpPwEKxcoqbv9ZjMry86fV58n9xB"),
                Arguments.of(100, "QDj1XtBhN4ejgbKLvqoEFB6wtvEvCfL5TzD69/Kw/NJXubz9WruX2JV8Kmr+QyqPfQ6AoqIq915Do0P3TehkvWBXYSVT2xfFT+wCAUcKlNJPHzF9LHPhWeLllqVEm959")
        );
    }

    @DisplayName("Should Throw IllegalArgumentException when trying to encrypt Null values.")
    @ParameterizedTest(name = "{index} => value={0}")
    @MethodSource("illegalValuesResource")
    void throwIllegalArgumentExceptionOnEncryption(Object value) {
        assertThrows(IllegalArgumentException.class, () -> AES.init().encrypt(value));
    }


    @DisplayName("Should Throw IllegalArgumentException when trying to decrypt Null values.")
    @ParameterizedTest(name = "{index} => value={0}")
    @MethodSource("illegalValuesResource")
    void throwIllegalArgumentExceptionOnDecryption(String value) {
        assertThrows(IllegalArgumentException.class, () -> AES.init().encrypt(value));
    }

    private static Stream<Arguments> illegalValuesResource() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null)
        );
    }
}
