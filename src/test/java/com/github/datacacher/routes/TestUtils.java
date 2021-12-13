package com.github.datacacher.routes;

import java.util.List;

public class TestUtils {
    private TestUtils() {
    }

    public static boolean isSorted(List<String> listOfStrings, int index) {
        if (index < 2) {
            return true;
        } else if (listOfStrings.get(index - 2).compareTo(listOfStrings.get(index - 1)) > 0) {
            return false;
        } else {
            return isSorted(listOfStrings, index - 1);
        }
    }
}
