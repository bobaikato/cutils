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

import org.apache.commons.lang3.Validate;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * <p>This class provide a means to partition List into List of sublist.</p>
 *
 * @author Szymon Stepniak
 * @author B0BAI
 * @since v1.0
 */
public class Partition<T> extends AbstractList<List<T>> {

    /**
     * <p> Variable to hold List</p>
     *
     * @since v1.0
     */
    private final List<T> list;

    /**
     * <p>variable to hold the sublist size.</p>
     *
     * @since v1.0
     */
    private int sublistSize;

    /**
     * <p>Sole constructor.</p>
     *
     * @param list List to be partitioned.
     */
    private Partition(List<T> list) {
        this.list = new ArrayList<>(list);
    }

    /**
     * <p>Method to receive the list to be partitioned</p>
     *
     * @param list List to be partitioned.
     * @param <T>  List type
     * @return instance of {@link Partition}
     */
    public static <T> Partition<T> of(List<T> list) {
        return new Partition<>(list);
    }

    /**
     * <p>Method used to get partition size</p>
     *
     * @param sublistSize The sub-list/Partition size.
     * @return current instance of {@link Partition}
     */
    public Partition<T> into(int sublistSize) {
        return Que.<Partition<T>>run(() -> {
            Validate.isTrue(sublistSize > 0, "Sub-list size must be greater than 0.");
        }).andRun(() -> this.sublistSize = sublistSize).andSupply(() -> this).get();
    }

    @Override
    public List<T> get(int index) {
        final int start = index * sublistSize;
        final int end = Math.min(start + sublistSize, list.size());

        return Que.<List<T>>run(() -> {
            if (start > end) {
                throw new IndexOutOfBoundsException(format("Index %d is out of the list range <0,%d>", index, this.size() - 1));
            }
        }).andSupply(() -> new ArrayList<>(list.subList(start, end))).get();
    }

    @Override
    public int size() {
        return (int) Math.ceil((double) list.size() / (double) sublistSize);
    }
}