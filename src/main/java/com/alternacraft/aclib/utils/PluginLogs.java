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
import com.alternacraft.aclib.PluginBase;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginLogs {

    private static String logs_folder = "logs";
    private static String default_path = "logs";

    private final List<String> messages;
    private final String fullpath;
    private final String path;

    /**
     * Register a logger which will be saved into plugin folder
     *
     * @param filename File name
     */
    public PluginLogs(String filename) {
        this(PluginLogs.default_path, filename);
    }

    /**
     * Register a logger which will be saved into path
     *
     * @param path Path
     * @param filename File name
     */
    public PluginLogs(String path, String filename) {
        this.path = path;
        this.messages = new ArrayList();
        this.fullpath = PluginLogs.default_path + filename;
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
        // Nothing to export
        if (messages.isEmpty()) {
            return;
        }
        
        // Creating logs folder if not exists
        if (!FileUtils.exists(path)) {
            if (!FileUtils.createDir(path)) {
                MessageManager.logError("Couldn't create " + logs_folder + " folder");
                return;
            }
        }

        String resul = "";

        if (keep_old_values) {
            // Recovering old values
            List<String> old_values = new ArrayList<>();
            if (FileUtils.exists(fullpath)) {
                old_values = FileUtils.getFileLines(fullpath);
                FileUtils.delete(fullpath);
            }

            // Writing old values            
            if (old_values.size() > 0) {
                for (String cont : old_values) {
                    resul += cont + "\n";
                }
                resul += "\n";
            }
        }

        // Writing new values        
        resul += "### " + DateUtils.getCurrentTimeStamp() + " ###\n";
        for (String message : messages) {
            resul += message + "\n";
        }
        FileUtils.writeFile(fullpath, resul);
    }

    //<editor-fold defaultstate="collapsed" desc="STATIC METHODS">
    /**
     * Get all custom logs
     *
     * @return File[]
     */
    public static File[] importLogs() {
        return PluginLogs.importLogs(default_path);
    }

    public static File[] importLogs(String path) {
        return FileUtils.getFilesIntoDir(path);
    }

    /**
     * Get a custom log
     *
     * @param logname Log name with extension
     * 
     * @return File
     */
    public static File importLog(String logname) {
        return PluginLogs.importLog(default_path, logname);
    }

    public static File importLog(String path, String logname) {
        File[] files = importLogs(path);
        for (File file : files) {
            if (file.getName().equals(logname)) {
                return file;
            }
        }
        return null;
    }

    /**
     * Remove a custom log
     *
     * @param name Log name with extension
     */
    public static void removeLog(String name) {
        PluginLogs.removeLog(PluginLogs.default_path, name);
    }

    public static void removeLog(String path, String name) {
        FileUtils.delete(path + name);
    }

    /**
     * Parse the content of a log file
     *
     * @param log Log file
     *
     * @return Map with the content parsed
     */
    public static Map<Date, List<String>> parseLogFile(File log) {
        Map<Date, List<String>> data = new HashMap<>();
        List<String> lines = FileUtils.getFileLines(log);

        Date lastdate = null;
        DateFormat format = DateUtils.getDefaultDateFormat();

        for (String line : lines) {
            if (line.matches("### .* ###")) {
                try {
                    lastdate = format.parse(line.replace("### ", "").replace(" ###", ""));
                    data.put(lastdate, new ArrayList());
                } catch (Exception ex) {
                    MessageManager.logError("Error parsing log: " + log.getName());
                }
            } else if (!line.isEmpty() && lastdate != null && data.containsKey(lastdate)) {
                data.get(lastdate).add(line);
            }
        }

        return data;
    }

    public static final String getLogsFolderName() {
        return logs_folder;
    }

    public static final void changeLogsFolderNameTo(String str) {
        logs_folder = str;
    }

    public static final void setDefaultPath(JavaPlugin plugin) {
        PluginLogs.default_path = PluginBase.DIRECTORY + logs_folder + File.separator;
    }
    
    public static final String getDefaultPath() {
        return PluginLogs.default_path;
    }
    //</editor-fold>
}
