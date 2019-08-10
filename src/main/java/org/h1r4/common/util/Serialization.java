package org.h1r4.common.util;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * <p>Assists with the serialization process and performs additional functionality based
 * on serialization.</p>
 *
 * <ul>
 * <li>Deep clone using serialization
 * <li>Serialize managing finally and IOException
 * <li>Deserialize managing finally and IOException
 * </ul>
 *
 * <p>This class throws exceptions for invalid {@code null} inputs.
 * Each method documents its behaviour in more detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @author B0BAI
 * @since 1.0
 */
public class Serialization extends SerializationUtils {

    /**
     * <p>Serializes an {@code Object} to a byte array</p>
     *
     * @param object the object to serialize to bytes
     * @return a byte[] with the converted Serializable
     * @throws java.io.IOException if the serialization fails
     * @since 1.0
     */
    public static byte[] serialize(Object object) throws Exception {
        return Que.<byte[]>run(() -> Validate.isTrue(isNotEmpty(object), "Object to serialize cannot be null."))
                .andCall(() -> {
                    final var outputStream = new ByteArrayOutputStream(512);
                    final var os = new ObjectOutputStream(outputStream);

                    return Que.<byte[]>execute(() -> os.writeObject(object)).andExecute(os::flush)
                            .andExecute(os::close).andSupply(outputStream::toByteArray).get();
                }).get();
    }
}
