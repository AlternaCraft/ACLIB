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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CustomLinkedMap<K, V> extends LinkedHashMap<K, V> {

    /**
     * Method for getting an entry by position
     *
     * @param i Position
     *
     * @return Map.Entry
     */
    public Map.Entry<K, V> getEntry(int i) {
        // check if negetive index provided
        Set<Map.Entry<K, V>> entries = entrySet();
        int j = 0;

        for (Map.Entry<K, V> entry : entries) {
            if (j++ == i) {
                return entry;
            }
        }

        return null;
    }

    /**
     * Method for getting a key by position
     *
     * @param i Position
     *
     * @return K
     */
    public K getKey(int i) {
        Map.Entry<K, V> entry = this.getEntry(i);
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    /**
     * Method for getting a value by position
     *
     * @param i Position
     *
     * @return V
     */
    public V getValue(int i) {
        K key = this.getKey(i);
        if (key == null) {
            return null;
        }
        return this.get(key);
    }

}
