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

/**
 * An utils class about numbers.
 *
 * @author AlternaCraft
 */
public class NumbersUtils {

    /**
     * Checks if an string is an Integer instance or not.
     *
     * @param s String
     * @return boolean
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a number is even or odd.
     *
     * @param number int
     * @return boolean
     */
    public static boolean isEven(int number) {
        return (number & 1) == 0;
    }

    /**
     * Method for getting the difference between two numbers
     *
     * @param p1 int
     * @param p2 int
     * @return distance
     *
     * @since 0.0.9
     */
    public static int differenceBetween(int p1, int p2) {
        int v = (p1 > p2) ? (p1 - p2) : (p2 - p1);
        return Math.abs(v);
    }

    /**
     * Method for getting the difference between two numbers
     *
     * @param p1 int
     * @param p2 int
     * 
     * @return distance
     */
    @Deprecated
    public static int distanceBetweenPoints(int p1, int p2) {
        return differenceBetween(p1, p2);
    }

    /**
     * Method for getting lower number
     *
     * @param numbers int...
     * @return lower
     */
    public static int getLower(int... numbers) {
        Integer lower = null;
        for (int number : numbers) {
            if (lower == null) {
                lower = number;
            } else if (number < lower) {
                lower = number;
            }
        }
        return lower;
    }
}