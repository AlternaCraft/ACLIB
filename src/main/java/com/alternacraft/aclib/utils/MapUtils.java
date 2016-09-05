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

import com.alternacraft.aclib.commands.CommandArgument;
import java.util.Map;
import java.util.Set;

public class MapUtils {

    /**
     * Method for getting the map keys as an array
     *
     * @param <K> Map key
     * @param <V> Map value
     * @param map Map
     * @return Key array
     */
    public static <K, V> K[] getKeys(Map<K, V> map) {
        return (K[]) map.keySet().toArray();
    }

    /**
     * Method for getting a key by value
     *
     * @param <K> Map key
     * @param <V> Map value
     * @param map Map
     * @param value value
     * @return Key
     */
    public static <K, V> K getKeyFrom(Map<K, V> map, V value) {
        if (map.containsValue(value)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                K k = entry.getKey();
                V v = entry.getValue();

                if (v.equals(value)) {
                    return k;
                }
            }
        }
        return null;
    }

    /**
     * Method for getting a map key by argument from CommandArgument
     *
     * @param <K> Map key
     * @param <V> Map value
     * @param map Map
     * @param argument String with the argument
     * 
     * @see CommandArgument
     * 
     * @return Key
     */
    public static <K extends CommandArgument, V> K findArgument(Map<K, V> map,
            String argument) {
        Set<K> keys = map.keySet();
        for (K key : keys) {
            if (key.getArgument().equals(argument)) {
                return key;
            }
        }
        return null;
    }

}
