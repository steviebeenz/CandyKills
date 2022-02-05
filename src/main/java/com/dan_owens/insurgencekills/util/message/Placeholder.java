package com.dan_owens.insurgencekills.util.message;


public class Placeholder {

    private final String key, value;

    public Placeholder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String apply(String string) {
        return string.replace(this.key, this.value);
    }

    public static String apply(String string, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            string = string.replace(placeholder.key, placeholder.value);
        }
        return string;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
