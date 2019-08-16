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

import com.honerfor.cutils.Que;
import org.apache.commons.lang3.Validate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static com.honerfor.cutils.Serialization.deserialize;
import static com.honerfor.cutils.Serialization.serialize;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static java.util.Objects.isNull;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * <p>
 * This is an implementation of AAdvanced Encryption Standard.
 * This implementation can encrypt and decrypt Objects of any type.
 * </p>
 *
 * @param <T> Type of value
 * @author B0BAI
 * @since 1.0
 */
public class AES<T> {

    /**
     * <p>This is the default Encryption key which will be used if {@link AES#encryptionKey} isn't set </p>
     *
     * @since 1.0
     */
    private static String ENCRYPTION_KEY = "{bMJR_QG$qvY?R*wGT2Sn9RU=GvKx_yu7Uyz^E*!*SjgaEh4K34JK8yTLB44!Z77R6z^DijHEi5GaaYA6apf3!}" +
            "This is clearly a default Key. If you are doing something serious, please consider" +
            "using the `AES.setKey(<Your_Key_here>)` method to set a unique Key. Got it?";

    /**
     * <p>Instance of {@link Cipher}</p>
     *
     * @since 1.0
     */
    private static Cipher cipher;

    /**
     * <p>Instance of {@link SecretKeySpec}</p>
     *
     * @since 1.0
     */
    private static SecretKeySpec secretKey;

    /**
     * <p>Encryption Key, set by user.</p>
     *
     * @implSpec If this is null, {@link AES#ENCRYPTION_KEY} will be used.
     * @since 1.0
     */
    private static String encryptionKey;

    /**
     * <p>Constructor</p>
     *
     * @throws NoSuchPaddingException   when a bad/Wrong encryption key is supplied.
     * @throws NoSuchAlgorithmException This exception is thrown when a cryptographic algorithm not available in the environment.
     * @since 1.0
     */
    private AES() throws NoSuchPaddingException, NoSuchAlgorithmException {
        var key = encryptionKey.getBytes(UTF_8);
        final var messageDigest = MessageDigest.getInstance("SHA-1");
        key = messageDigest.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
        cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    }

    /**
     * <p>This initiates encryption variables</p>
     *
     * @param <T> Type of value
     * @return Instance of {@link AES}
     * @throws Exception instance {@link NoSuchAlgorithmException}, {@link NoSuchPaddingException}
     * @since 1.0
     */
    public static <T> AES<T> init() throws Exception {
        return Que.<AES<T>>run(() -> encryptionKey = ENCRYPTION_KEY).andCall(AES::new);
    }

    /**
     * <p>
     * Use this method to set costume encryptionKey
     * If {@code key} is {@literal null}, {@link AES#ENCRYPTION_KEY} will be used instead.
     * </p>
     *
     * @param key supplied encryption key.
     * @param <T> Type of value
     * @return Instance of {@link AES}
     * @throws Exception instance {@link NoSuchAlgorithmException}, {@link NoSuchPaddingException}
     * @since 1.0
     */

    public static <T> AES<T> setKey(String key) throws Exception {
        return Que.<AES<T>>run(() -> encryptionKey = isNull(key) ? ENCRYPTION_KEY : key).andCall(AES::new);
    }

    /**
     * <p>This encrypt item of T type</p>
     *
     * @param itemToEncrypt item to encrypt.
     * @return encrypted string of {@code itemToEncrypt} of T type. Not {@literal null}
     * @throws Exception instance of any exception thrown
     * @since 1.0
     */
    public String encrypt(@Valid T itemToEncrypt) throws Exception {
        final Callable<String> encrypt = () -> Que.<String>execute(() -> cipher.init(Cipher.ENCRYPT_MODE, secretKey))
                .andCall(() -> {
                    final var serializeData = serialize(itemToEncrypt);
                    return getEncoder().encodeToString(cipher.doFinal(serializeData));
                });

        return Que.<String>run(() -> Validate.isTrue(isNotEmpty(itemToEncrypt), "Item to encrypt cannot be null.", itemToEncrypt)).andCall(encrypt);
    }

    /**
     * <p>This method will decrypt the {@code itemToDecrypt}</p>
     *
     * @param itemToDecrypt encrypted string to be decrypted. not {@literal null}
     * @return decrypted Object.
     * @throws Exception instance of {@link InvalidKeyException}, {@link BadPaddingException} or any other exception thrown.
     * @since 1.0
     */

    public T decrypt(@NotNull String itemToDecrypt) throws Exception {
        return Que.<T>run(() -> {
            Validate.isTrue(isNotEmpty(itemToDecrypt), "Item to decrypt cannot be null.", itemToDecrypt);
        }).andCall(() -> Que.<T>execute(() -> {
            cipher.init(DECRYPT_MODE, secretKey);
        }).andCall(() -> {
            return deserialize(cipher.doFinal(getDecoder().decode(itemToDecrypt)));
        }));
    }
}