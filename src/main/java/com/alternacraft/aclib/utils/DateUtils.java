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
package com.alternacraft.aclib.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class contains some utils for managing Dates.
 *
 * @author AlternaCraft
 */
public class DateUtils {

    private final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Method for getting current time stamp with the default format
     * <i>Check out the constant DEFAULT_FORMAT</i>
     *
     * @return String
     */
    public static String getCurrentTime() {
        return getCurrentTime(DEFAULT_FORMAT);
    }

    /**
     * Method for getting current time stamp with a custom format
     *
     * @param format String with format
     * @return String
     * @see SimpleDateFormat
     */
    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * Return the default date format.
     *
     * @return DateFormat
     */
    public static DateFormat getDefaultDateFormat() {
        return new SimpleDateFormat(DEFAULT_FORMAT);
    }

    public static boolean sameDate(Date di, Date d2) {
        Calendar today = Calendar.getInstance();
        Calendar last = Calendar.getInstance();

        today.setTime(new Date());
        last.setTime(d2);

        return today.get(Calendar.YEAR) == last.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == last.get(Calendar.DAY_OF_YEAR);
    }
}
