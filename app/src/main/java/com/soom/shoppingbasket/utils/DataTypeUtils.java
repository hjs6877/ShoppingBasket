package com.soom.shoppingbasket.utils;

/**
 * Created by kjs on 2017-01-20.
 */

public class DataTypeUtils {
    public static int convertBooleanToInt(boolean value){
        return value ? 1 : 0;
    }
    public static boolean convertIntToBoolean(int value){
        return value == 1 ? true : false;
    }
}
