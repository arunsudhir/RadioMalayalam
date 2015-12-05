package com.arunsudhir.radiomalayalam.sql;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A helper to create execute conditions
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Condition {
    private final String condition;
    private final String[] arguments;

    private <T> Condition(String columnName, String operator, T value) {
        this(String.format("%s %s ?", columnName, operator), new String[]{value.toString()});
    }

    public static <T> Condition eq(String column, T value) {
        return new Condition(column, "=", value);
    }

    public static <T> Condition neq(String column, T value) {
        return new Condition(column, "!=", value);
    }

    public static <T> Condition like(String column, T value) {
        return new Condition(column, "like", value);
    }

    public static Condition where(String condition, String... arguments) {
        return new Condition(condition, arguments);
    }
}
