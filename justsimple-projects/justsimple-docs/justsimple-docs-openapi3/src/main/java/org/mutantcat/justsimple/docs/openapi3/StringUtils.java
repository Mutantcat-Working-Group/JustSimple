package org.mutantcat.justsimple.docs.openapi3;

public class StringUtils {

    public static String getOrDefault(String value, String defaultValue) {
        return value == null || value.isEmpty() ? defaultValue : value;
    }

}
