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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class allows you to record times for managing performance.
 * 
 * @author AlternaCraft
 */
public class Timer {

    private final Map<String, Long> timeAtStart = new HashMap();
    private final Map<String, List<Long>> register = new HashMap();

    /**
     * Save the current time for "id".
     * 
     * @param id Name for the process
     */
    public void start(String id) {
        this.timeAtStart.put(id, System.currentTimeMillis());
    }

    /**
     * Returns the start time for "id"
     * 
     * @param id Process name
     * 
     * @return Time in milliseconds
     */
    public long getStartTime(String id) {
        return this.timeAtStart.get(id);
    }

    /**
     * Returns the difference between the final time and the start time.
     * Also, it saves the value into the variable: "register".
     * 
     * @param id Process name
     * 
     * @return Time in milliseconds
     */
    public long recordValue(String id) {
        long finalTime = System.currentTimeMillis();

        if (!this.register.containsKey(id)) {
            this.register.put(id, new ArrayList());
        }

        long elapsedtime = finalTime - getStartTime(id);
        this.register.get(id).add(elapsedtime);
        return elapsedtime;
    }

    /**
     * Returns parsed values.
     * <i>The values will be replaced with the mean</i>
     * 
     * @return Parsed values
     */
    public Map<String, Integer> getParsedValues() {
        Map<String, Integer> parsed = new HashMap<>();
        
        for (Map.Entry<String, List<Long>> entry : register.entrySet()) {
            String key = entry.getKey();
            List<Long> value = entry.getValue();

            int size = value.size();
            int total = 0;
            for (Long record : value) {
                total += record;
            }
            total /= size;
            
            parsed.put(key, total);
        }
        
        return parsed;
    }

    /**
     * Save the messages to a log file.
     * <i>If the file already exists will be deleted</i>
     * 
     * @param filename File to export the content
     */
    public void saveToLog(String filename) {
        PluginLog pf = new PluginLog(filename);

        for (Map.Entry<String, Integer> entry : this.getParsedValues().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            pf.addMessage(key + " - " + value);
        }

        pf.export(false);
    }

    //<editor-fold defaultstate="collapsed" desc="CLASS STUFF">
    private static int getAverageInMillis(List<Long> times) {
        int x = 0;

        for (Long l : times) {
            x += l;
        }

        return (x /= times.size());
    }
    //</editor-fold>    
}
