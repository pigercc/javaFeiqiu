package com.pigercc.feiqiu;

/**
 * Created by pigercc on 2016/6/19.
 */
public class StringUtil {

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }
}
