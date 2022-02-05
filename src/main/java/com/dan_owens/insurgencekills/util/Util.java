package com.dan_owens.insurgencekills.util;

import org.bukkit.ChatColor;

import java.text.DecimalFormat;

public class Util {

    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,##0");

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


    public static String getFormattedNumber(double number){
        return NUMBER_FORMAT.format(number);
    }

}
