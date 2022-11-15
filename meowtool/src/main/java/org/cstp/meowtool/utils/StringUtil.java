package org.cstp.meowtool.utils;

public class StringUtil {
    /**
     * Judge if a string is refering to null or to an empty string.
     * 
     * @param target The string needs to be judged.
     * @return {@code true} if the target is null or empty, otherwise {@code false}.
     */
    public static boolean isNullOrEmpty(String target) {
        return target == null || target.compareTo("") == 0;
    }
}
