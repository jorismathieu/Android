package fr.zait.utils;

public class StringUtils
{
    public static boolean isEmpty(String str) {
        if (str != null && !str.equals("")) {
            return false;
        }
        return true;
    }
}
