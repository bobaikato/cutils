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

package com.honerfor.cutils;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Validate;

/**
 * <p>
 * Assists with the serialization process and performs additional functionality based
 * on serialization.
 * </p>
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
   * <p>Serializes an {@code Object} to a byte array.</p>
   *
   * @param object the object to serialize to bytes
   * @return a byte[] with the converted Serializable
   * @throws java.io.IOException if the serialization fails
   * @since 1.0
   */
  public static byte[] serialize(Object object) throws Exception {
    return Que.<byte[]>run(() -> {
      Validate.isTrue(isNotEmpty(object), "Object to serialize cannot be null.");
    }).andCall(() -> {
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
      final ObjectOutputStream os = new ObjectOutputStream(outputStream);
      return Que.<byte[]>execute(() -> os.writeObject(object))
        .andExecute(os::flush)
        .andExecute(os::close)
        .andSupply(outputStream::toByteArray)
        .get();
    }).get();
  }
}
