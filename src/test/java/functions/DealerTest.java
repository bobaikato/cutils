/*
 * Copyright (C) 2018 — 2019 Honerfor, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package functions;

import com.honerfor.cutils.function.Dealer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class DealerTest {

    @SneakyThrows
    @DisplayName("Expect Dealer to return variable of same Type that was Initialized.")
    @ParameterizedTest(name = "{index} => value={0}")
    @MethodSource("firstDealerFunctions")
    void verifyDealerReturnsExpectedVariableOfSameType(Dealer<?> dealer) {

        assertNotNull(dealer.deal());

        if (dealer.deal() instanceof Integer) {
            assertEquals(dealer.deal(), 122);
        }

        if (dealer.deal() instanceof String) {
            assertEquals(dealer.deal(), "flash");
        }
    }

    private static Stream<Arguments> firstDealerFunctions() {
        final Dealer<String> firstDealer = () -> "flash";
        final Dealer<Integer> secondDealer = () -> 122;

        return Stream.of(
                Arguments.of(firstDealer),
                Arguments.of(secondDealer)
        );
    }

    @DisplayName("Expect Dealer throws an Exception.")
    @ParameterizedTest(name = "{index} => value={0}")
    @MethodSource("secondDealerFunctions")
    void verifyDealerThrowsAnException(Dealer<?> dealer) {
        Assertions.assertThrows(Exception.class, dealer::deal);
    }

    private static Stream<Arguments> secondDealerFunctions() {
        final Dealer<Class<?>> firstDealer = () -> {
            throw new ClassCastException();
        };

        final Dealer<File> secondDealer = () -> {
            throw new IllegalAccessException();
        };

        final Dealer<String> thirdDealer = () -> {
            throw new NullPointerException();
        };

        return Stream.of(
                Arguments.of(firstDealer),
                Arguments.of(secondDealer),
                Arguments.of(thirdDealer)
        );
    }
}
