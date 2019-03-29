/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.aclib.extras;

import org.bukkit.util.Vector;

import java.util.Random;

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
     * Method for getting the difference between two numbers.
     *
     * @param p1 int
     * @param p2 int
     * @return distance
     * @since 0.0.9
     */
    public static int differenceBetween(int p1, int p2) {
        int v = (p1 > p2) ? (p1 - p2) : (p2 - p1);
        return Math.abs(v);
    }

    /**
     * Method for getting lower number.
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

    /**
     * Returns if a number is equals or between two other values.
     *
     * @param value  Number to analyze
     * @param lower  Lower value
     * @param bigger Bigger value
     * @return True if it is equals or between two other number; False if not
     */
    public static boolean isEqualsOrBetween(float value, float lower, float bigger) {
        return value >= lower && value <= bigger;
    }

    /**
     * Returns if a number is between two other values.
     *
     * @param value  Number to analyze
     * @param lower  Lower value
     * @param bigger Bigger value
     * @return True if it is between two other number; False if not
     */
    public static boolean isBetween(float value, float lower, float bigger) {
        return value > lower && value < bigger;
    }

    /**
     * Get a random value between an interval.
     *
     * @param max Max value
     * @param min Min value
     * @return int
     */
    public static int rand(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    /**
     * Get a random vector contained into the two vectors.
     *
     * @param p1 Vector 1
     * @param p2 Vector 2
     * @return Random vector
     * @since 0.0.9
     */
    public static Vector randBetweenVector(Vector p1, Vector p2) {
        int pos1, pos2, pos3;

        pos1 = seekMAXandGetRand(p1.getBlockX(), p2.getBlockX());
        pos2 = seekMAXandGetRand(p1.getBlockY(), p2.getBlockY());
        pos3 = seekMAXandGetRand(p1.getBlockZ(), p2.getBlockZ());

        return new Vector(pos1, pos2, pos3);
    }

    private static int seekMAXandGetRand(int v1, int v2) {
        int r;

        if (v1 > v2) {
            r = rand(v2, v1);
        } else if (v1 == v2) {
            r = v1;
        } else {
            r = rand(v1, v2);
        }

        return r;
    }
}
