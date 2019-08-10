import org.h1r4.common.util.Que;
import org.h1r4.common.util.security.AES;
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
class AESTest {

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

    @DisplayName("Should successfully Encrypt Custom Object with default Encryption Key")
    @ParameterizedTest(name = "{index} => objectToBeEncrypted={0}, encryptedString={1}")
    @MethodSource("customObjectEncryptionResource")
    void encryptCustomObjectWithDefaultKey(PersonExample objectToBeEncrypted, String encryptedString) throws Exception {
        assertEquals(AES.<PersonExample>init().encrypt(objectToBeEncrypted), encryptedString);
    }

    private static Stream<Arguments> customObjectEncryptionResource() {
        final var personExampleI = new PersonExample() {{
            setAge(10);
            setName("B0B");
        }};

        final var personExampleII = new PersonExample() {{
            setAge(11);
            setName("PETER");
        }};

        return Stream.of(
                Arguments.of(personExampleI, "k/h51sKoS2yWgcKvNhliYzA6kk8zDzYpyo85kc9pIGPubzz9sh1vu1SsYgb6Q8RZmeXrtK57KSUDM4k7IGTDx3PyZ4bO9aI6O3MvXNTdFJRYckqsFTrd44/SITmttWuQQTCURIIDi+9M6hmOlzDlOw=="),
                Arguments.of(personExampleII, "k/h51sKoS2yWgcKvNhliY3WL2bHdh+kr98PishwlCxDubzz9sh1vu1SsYgb6Q8RZmeXrtK57KSUDM4k7IGTDx3PyZ4bO9aI6O3MvXNTdFJRYckqsFTrd44/SITmttWuQ7GZFcqV1QoCyO1ZXkaVY1q/ufFS7PBuKBEutuQn8fQM=")
        );
    }

    @DisplayName("Should successfully Decrypt Custom Object with default Encryption Key")
    @ParameterizedTest(name = "{index} => output={0}, objectToBeDecrypted={1}")
    @MethodSource("customObjectDecryptionResource")
    void decryptCustomObjectWithDefaultKey(PersonExample output, String objectToBeDecrypted) throws Exception {
        final var decryptedObject = AES.<PersonExample>init().decrypt(objectToBeDecrypted);
        Que.run(() -> assertEquals(decryptedObject.age, output.age))
                .andRun(() -> assertEquals(decryptedObject.name, output.name));
    }

    private static Stream<Arguments> customObjectDecryptionResource() {
        final var personExampleI = new PersonExample() {{
            setAge(10);
            setName("B0B");
        }};

        final var personExampleII = new PersonExample() {{
            setAge(11);
            setName("PETER");
        }};

        return Stream.of(
                Arguments.of(personExampleI, "k/h51sKoS2yWgcKvNhliYzA6kk8zDzYpyo85kc9pIGPubzz9sh1vu1SsYgb6Q8RZmeXrtK57KSUDM4k7IGTDx3PyZ4bO9aI6O3MvXNTdFJRYckqsFTrd44/SITmttWuQQTCURIIDi+9M6hmOlzDlOw=="),
                Arguments.of(personExampleII, "k/h51sKoS2yWgcKvNhliY3WL2bHdh+kr98PishwlCxDubzz9sh1vu1SsYgb6Q8RZmeXrtK57KSUDM4k7IGTDx3PyZ4bO9aI6O3MvXNTdFJRYckqsFTrd44/SITmttWuQ7GZFcqV1QoCyO1ZXkaVY1q/ufFS7PBuKBEutuQn8fQM=")
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
        assertEquals(AES.setKey("My-Custom-Key").encrypt(itemToBeEncrypted), encryptedString);
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
    @MethodSource("customKeyDecryptionResource")
    void decryptObjectWithCustomKey(Object output, String itemToBeDecrypted) throws Exception {
        assertEquals(AES.setKey("My-Custom-Key").decrypt(itemToBeDecrypted), output);
    }

    private static Stream<Arguments> customKeyDecryptionResource() {
        return Stream.of(
                Arguments.of("Testing Decryption", "s68hIJWxSG09ZQjbGF/6oQ4c2a8wHXnHPbR92wV2PQk="),
                Arguments.of(88.02, "4OkXLhHBDmq54mtV2fp+kTj4xtONwwKNUx6rjppRT0b/E/WueHmSEwGiAYXRPRwQCEEWOcTcW+p1BRNnraepj9+0m+rBHV6y9rYG23WjWvj8aFusIpAE7+Ei61R1qdjD"),
                Arguments.of(12L, "sFaXqId3I2kIVEgrLjdRw7pVdlfITHrLJIq3LkkLCTFUR64LJymRedu7Ez+ULbjvq9xZw2Fhei0SXu4O6WCF800jKhHkXp26VPLZOpPwEKxcoqbv9ZjMry86fV58n9xB"),
                Arguments.of(100, "QDj1XtBhN4ejgbKLvqoEFB6wtvEvCfL5TzD69/Kw/NJXubz9WruX2JV8Kmr+QyqPfQ6AoqIq915Do0P3TehkvWBXYSVT2xfFT+wCAUcKlNJPHzF9LHPhWeLllqVEm959")
        );
    }


    @DisplayName("Should Throw BadPaddingException when using a different key to decrypt.")
    @ParameterizedTest(name = "{index} => output={0}, itemToBeDecrypted={1}")
    @MethodSource("decryptionResource")
    void throwBadPaddingExceptionOnDecryption(Object output, String itemToBeDecrypted) throws Exception {
        assertThrows(BadPaddingException.class, () -> {
            assertEquals(AES.setKey("The-Wrong-Custom-Encryption-Key").decrypt(itemToBeDecrypted), output);
        });
    }

    private static Stream<Arguments> decryptionResource() {
        return Stream.of(
                Arguments.of("Testing Decryption", "s68hIJWxSG09ZQjbGF/6oQ4c2a8wHXnHPbR92wV2PQk="),
                Arguments.of(88.02, "4OkXLhHBDmq54mtV2fp+kTj4xtONwwKNUx6rjppRT0b/E/WueHmSEwGiAYXRPRwQCEEWOcTcW+p1BRNnraepj9+0m+rBHV6y9rYG23WjWvj8aFusIpAE7+Ei61R1qdjD"),
                Arguments.of(12L, "sFaXqId3I2kIVEgrLjdRw7pVdlfITHrLJIq3LkkLCTFUR64LJymRedu7Ez+ULbjvq9xZw2Fhei0SXu4O6WCF800jKhHkXp26VPLZOpPwEKxcoqbv9ZjMry86fV58n9xB"),
                Arguments.of(100, "QDj1XtBhN4ejgbKLvqoEFB6wtvEvCfL5TzD69/Kw/NJXubz9WruX2JV8Kmr+QyqPfQ6AoqIq915Do0P3TehkvWBXYSVT2xfFT+wCAUcKlNJPHzF9LHPhWeLllqVEm959")
        );
    }
}
