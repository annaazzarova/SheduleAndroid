package com.example.anna.shedule.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String join(Iterable<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s);
            sep = separator;
        }
        return sb.toString();
    }

    public static List<String> split(String string, char separator) {
        ArrayList<String> arr = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char at = string.charAt(i);
            if (at == separator) {
                arr.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(at);
            }
        }
        arr.add(sb.toString());
        return arr;
    }
}
