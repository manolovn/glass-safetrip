package com.google.glass.glass_safetrip.util;

/**
 * glass-safetrip
 * com.google.glass.glass_safetrip.util
 *
 * @autor manolo
 */
public class StringUtil {

    public static String ArrayStringToString(Iterable<String> list) {

        String text = "";
        for (String s : list) {
            text += s + "\t";
        }

        return text;
    }

}
