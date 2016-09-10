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
     * Method for getting a formatted time
     *
     * @param s Time in seconds
     * @return String formatted as, for example, "5h 3m 2s" without quotes
     * 
     * @since 0.0.9
     */
    public static String splitToComponentTimes(int s) {
        String resul = "";

        // Logic
        int hours = (int) s / 3600;
        int remainder = (int) s - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        // Representation
        resul += (hours > 0) ? (hours + "h ") : "";
        if (hours > 0) {
            resul += mins + "m ";
        } else {
            resul += (mins > 0) ? (mins + "m ") : "";
        }
        resul += secs + "s";

        return resul;
    }
}
