package com.weswu.clouduuid.utils;

import java.util.Collection;
import java.util.function.Predicate;

public class Miscs {

    public static <T> T findByProperty(Collection<T> col, Predicate<T> filter) {
        return col.stream().filter(filter).findFirst().orElse(null);
    }
}
