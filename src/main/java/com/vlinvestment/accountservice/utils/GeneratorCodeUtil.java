package com.vlinvestment.accountservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeneratorCodeUtil {

    public static String generateNumber() {
        return String.valueOf(IntStream.generate(() -> (int) (Math.random() * 9000) + 1000).findFirst().getAsInt());
    }

}
