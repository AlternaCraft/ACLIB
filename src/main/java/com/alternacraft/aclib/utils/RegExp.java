/*
 * Copyright (C) 2017 AlternaCraft
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils for Regular Expressions
 * 
 * @author AlternaCraft
 */
public class RegExp {

    /**
     * variable ESCAPE_STRING to escape a character adding \ at the beginning
     */
    public static final String ESCAPE_STRING = "(?<!\\\\)";
    
    /**
     * Escape a String by adding ESCAPE_STRING at the beginning.
     * 
     * @param v String
     * 
     * @return Escaped String
     */
    public static String escape(String v) {
        return ESCAPE_STRING + v;
    }

    /**
     * Quote and Escape a String by adding ESCAPE_STRING at the beginning.
     * 
     * @param v String
     * 
     * @return Escaped and Quoted String
     */    
    public static String QaE(String v) {
        return escape(Pattern.quote(v));
    }
    
    /**
     * Get groups from an input applying a pattern.
     * 
     * @param pattern Pattern
     * @param input Input
     * 
     * @return Groups' list
     */
    public static List<String> getGroups(String pattern, String input) {
        List<String> data = new ArrayList<>();
        
        Pattern vars = Pattern.compile(pattern);
        Matcher matcher = vars.matcher(input);
        while (matcher.find()) {
            data.add(matcher.group());
        }
        
        return data;
    }

    /**
     * Get groups with elements from an input applying a pattern.
     * 
     * @param pattern Pattern
     * @param input Input
     * @param grs Groups which have to be returned in natural order; All if empty
     * 
     * @return Groups' list which include an array with the elements
     */    
    public static List<String[]> getGroupsWithElements(String pattern, String input, 
            int... grs) {
        List<String[]> data = new ArrayList<>();
        
        Pattern v = Pattern.compile(pattern);
        Matcher m = v.matcher(input);
        
        Arrays.sort(grs);        
        
        while (m.find()) {
            boolean spc = grs.length > 0 && grs[grs.length - 1] <= m.groupCount();
            String[] elements;
            if (spc) {
                elements = new String[grs.length];
            } else {
                elements = new String[m.groupCount()];                
            }
            for (int i = 0; i < elements.length; i++) {
                elements[i] = (spc) ? m.group(grs[i]) : m.group(i+1);
            }
            data.add(elements);
        }
        
        return data;
    }    
}
