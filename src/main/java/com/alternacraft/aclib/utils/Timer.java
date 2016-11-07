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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;

public class Timer {

    private final Map<String, Long> timeAtStart = new HashMap();
    private final Map<String, List<Long>> register = new HashMap();

    public void start(String id) {
        this.timeAtStart.put(id, System.currentTimeMillis());
    }

    public long getStartTime(String id) {
        return this.timeAtStart.get(id);
    }

    public void recordValue(String id) {
        long finalTime = System.currentTimeMillis();

        if (!this.register.containsKey(id)) {
            this.register.put(id, new ArrayList());
        }

        this.register.get(id).add((finalTime - this.timeAtStart.get(id)));
    }

    public String showAverage() {
        String v = ChatColor.YELLOW + "(Average) Load time of each process...\n";

        for (Map.Entry<String, List<Long>> entry : register.entrySet()) {
            String key = entry.getKey();
            List<Long> times = entry.getValue();

            v += key + " (" + StringsUtils.splitToComponentTimes(
                    (int) (getAverageInMillis(times) / 1000)) + "); ";
        }

        return v;
    }

    public void saveToLog(String filename) {
        PluginLog pf = new PluginLog(filename);

        for (Map.Entry<String, List<Long>> entry : register.entrySet()) {
            String key = entry.getKey();
            List<Long> value = entry.getValue();

            int size = value.size();
            int total = 0;
            for (Long record : value) {
                total += record;
            }
            total /= size;

            pf.addMessage(key + " - " + total);
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
