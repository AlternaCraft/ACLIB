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

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * Method for getting current time stamp with the default format
     * <i>Check out the constant DEFAULT_FORMAT</i>
     * 
     * @return String
     */
    public static String getCurrentTimeStamp() {
        return getCurrentTimeStamp(DEFAULT_FORMAT);
    }
    
    /**
     * Method for getting current time stamp with a custom format
     *
     * @param format String with format
     * 
     * @see SimpleDateFormat
     * @return String
     */
    public static String getCurrentTimeStamp(String format) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }    
}
