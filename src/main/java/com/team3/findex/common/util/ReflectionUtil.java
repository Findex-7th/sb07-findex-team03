package com.team3.findex.common.util;

import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {
    public static List<Object> dtoToValueList(Object dto) {
        return Arrays.stream(dto.getClass().getDeclaredFields())
            .peek(f -> f.setAccessible(true))
            .map(f -> {
                try {
                    return f.get(dto);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }
}
