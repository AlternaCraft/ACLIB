/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alternacraft.aclib.utils;

import org.bukkit.ChatColor;

/**
 * This class contains some utils for Strings
 *
 * @author AlternaCraft
 */
public class StringsUtils {

    public static String translateColors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String stripColors(String s) {
        return ChatColor.stripColor(s);
    }

    /**
     * Method for formatting in time components
     *
     * @param s Time in seconds
     * @return String formatted as, for example, "4d 5h 3m 2s" without quotes
     *
     * @since 0.0.9
     */
    public static String splitToComponentTimes(int s) {
        String resul = "";
        int days, hours, mins, secs, remainder;
        
        // Lógica        
        days = s / 3600 / 24;
        remainder = s - days * 3600 * 24;
        hours = remainder / 3600;        
        remainder = remainder - hours * 3600;
        mins = remainder / 60;
        remainder = remainder - mins * 60;
        secs = remainder;

        // Representación
        resul += (days > 0) ? (days + "d ") : "";
        if (days > 0) {
            resul += hours + "h ";
            resul += mins + "m ";
        }
        else {
            resul += (hours > 0) ? (hours + "h ") : "";
            if (hours > 0) {
                resul += mins + "m ";
            } else {
                resul += (mins > 0) ? (mins + "m ") : "";
            }
        }
        resul += secs + "s";

        return resul;
    }

    /**
     * Method for getting the higher length of multiple Strings
     *
     * @param strs String...
     *
     * @return the higher length
     */
    public static int getHigherLength(String... strs) {
        int max = 0;
        for (String str : strs) {
            str = stripColors(str);

            if (stripColors(str).length() > max) {
                max = str.length();
            }
        }
        return max;
    }

    /**
     * @since 0.0.9
     */
    public static enum POSITION {
        LEFT, CENTER, RIGHT;
    }

    /**
     * Method for setting the same length into multiple Strings by using a char
     * <p>
     * So far, this is a bit useless because lettes in Minecraft don't occupy
     * the same space</p>
     *
     * @param size Maximum size
     * @param e Char to add
     * @param p Position in the 'block'
     * @param strs String...
     *
     * @return String[] in the same order
     *
     * @since 0.0.9
     */
    public static String[] copyLength(int size, char e, POSITION p, String... strs) {
        boolean left;

        for (int i = 0; i < strs.length; i++) {
            int length = stripColors(strs[i]).length();
            left = true;

            for (int j = length; j < size; j++) {
                switch (p) {
                    case LEFT:
                        strs[i] = e + strs[i];
                        break;
                    case CENTER:
                        strs[i] = (left) ? (e + strs[i]) : (strs[i] + e);
                        left = !left;
                        break;
                    case RIGHT:
                        strs[i] += e;
                        break;
                }
            }
        }

        return strs;
    }
}
