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

import com.alternacraft.aclib.utils.PluginLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class allows you to record times for managing performance.
 * 
 * @author AlternaCraft
 */
public class Recorder {

    // If you want to measure times
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
     * Saves the value and returns the difference between the final time and 
     * the start time.
     * 
     * @param id Process name
     * 
     * @return Time in milliseconds
     */
    public long recordTime(String id) {
        long finalTime = System.currentTimeMillis();

        if (!this.register.containsKey(id)) {
            this.register.put(id, new ArrayList());
        }

        long elapsedtime = finalTime - getStartTime(id);
        this.register.get(id).add(elapsedtime);
        return elapsedtime;
    }
    
    /**
     * Saves the value.
     * 
     * @param id Process name
     * @param value Number to save
     */
    public void recordNumber(String id, long value) {
        if (!this.register.containsKey(id)) {
            this.register.put(id, new ArrayList());
        }
        this.register.get(id).add(value);
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
            
            int total = getAverage(value);
            
            parsed.put(key, total);
        }
        
        return parsed;
    }

    public void saveToLog(String filename) {
        this.saveToLog(new PluginLog(filename), false);
    }
    
    /**
     * Save the messages to a log file.
     * <i>If the file already exists will be deleted</i>
     * 
     * @param filename File to export the content
     * @param keep Keep previous data
     */    
    public void saveToLog(String filename, boolean keep) {
        this.saveToLog(new PluginLog(filename), keep);
    }
    
    public void saveToLog(PluginLog pl) {
        this.saveToLog(pl, false);
    }
    
    /**
     * Save the messages to a log file.
     * <i>If the file already exists will be deleted</i>
     * 
     * @param pl PluginLog
     * @param keep Keep previous data
     */
    public void saveToLog(PluginLog pl, boolean keep) {
        this.getParsedValues().entrySet().stream().forEach(entry -> {
            String key = entry.getKey();
            Integer value = entry.getValue();
            pl.addMessage(key + " - " + value);
        });
        pl.export(keep);
    }

    //<editor-fold defaultstate="collapsed" desc="CLASS STUFF">
    private static int getAverage(List<Long> times) {
        int x = 0;

        for (Long l : times) {
            x += l;
        }

        return x / times.size();
    }
    //</editor-fold>    
}
