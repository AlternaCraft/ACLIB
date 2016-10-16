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

import com.alternacraft.aclib.MessageManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginLogs {

    private static String logs_folder = "Logs";
    
    private final List<String> messages;
    private final String fullpath;
    private final String path;

    /**
     * Register a logger which will be saved into plugin folder
     *
     * @param pl JavaPlugin
     * @param filename File name
     */
    public PluginLogs(JavaPlugin pl, String filename) {
        this(pl.getDataFolder() + File.separator, filename);
    }

    /**
     * Register a logger which will be saved into path
     * 
     * @param path Path
     * @param filename File name
     */
    public PluginLogs(String path, String filename) {
        this.messages = new ArrayList();
        this.path = path + logs_folder + File.separator;
        this.fullpath = this.path + filename;
    }

    /**
     * Add a new record to log
     * 
     * @param str Record value
     */
    public void addMessage(String str) {
        if (!messages.contains(str)) {
            messages.add(str);
        }
    }

    /**
     * Export records to log file
     * 
     * @param keep_old_values Keep old values?
     */
    public void export(boolean keep_old_values) {
        // Creating log folder if not exists
        if (!UtilsFile.exists(path)) {
            if (!UtilsFile.createDir(path)) {
                MessageManager.logError("Couldn't create Logs folder");
                return;
            }
        }
        
        String all = "";
        
        if (keep_old_values) {
            // Recovering old values
            List<String> old_values = new ArrayList<>();        
            if (UtilsFile.exists(fullpath)) {
                old_values = UtilsFile.getFileLines(fullpath);
                UtilsFile.delete(fullpath);
            }

            // Writing old values            
            if (old_values.size() > 0) {
                for (String cont : old_values) {
                    all += cont + "\n";
                }
                all += "\n";            
            }
        }
        
        // Writing new values
        // Date
        all += "---\n" + DateUtils.getCurrentTimeStamp() + "\n---\n";
        for (String message : messages) {
            all += message + "\n";
        }
        UtilsFile.writeFile(fullpath, all);
    }
    
    public static String getLogsFolderName() {
        return logs_folder;
    }
    
    public static void changeLogsFolderNameTo(String str) {
        logs_folder = str;
    }
}
