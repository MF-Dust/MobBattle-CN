package me.mxtery.mobbattle.helpers;

import java.util.List;
import java.util.stream.Collectors;

public class StringFormatter {
    public static String condenseString(List<String> list) {
        return list.stream()
                .distinct()
                .map(creature -> creature + " (x" + list.stream().filter(c -> c.equals(creature)).count() + ")")
                .collect(Collectors.joining(", "));
    }
}
