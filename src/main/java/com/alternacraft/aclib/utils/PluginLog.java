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

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Useful class for managing plugin logs.
 * 
 * @author AlternaCraft
 */
public class PluginLog extends File {

    private static String logs_folder = "logs";
    private static String default_path = "";

    private final List<String> messages;

    /**
     * Register a logger which will be saved into plugin folder.
     *
     * @param filename File name with extension
     */
    public PluginLog(String filename) {        
        this(PluginLog.default_path, filename);
    }

    /**
     * Register a logger which will be saved into path.
     *
     * @param path Path without the last slash
     * @param filename File name with extension
     */
    public PluginLog(String path, String filename) {
        super(path + File.separator + filename);
        this.messages = new ArrayList();
    }

    /**
     * Add a new record to log.
     *
     * @param str Record value
     */
    public void addMessage(String str) {
        if (!messages.contains(str)) {
            messages.add(str);
        }
    }

    /**
     * Export records to log file.
     *
     * @param keep_old_values Keep old values?
     */
    public void export(boolean keep_old_values) {
        // Nothing to export
        if (messages.isEmpty()) {
            return;
        }

        // Creating logs folder if not exists
        if (!this.getParentFile().exists()) {
            if (!this.getParentFile().mkdirs()) {
                MessageManager.logError("Couldn't create Logs folder");
                return;
            }
        }

        String resul = "";

        if (keep_old_values) {
            // Recovering old values
            List<String> old_values = new ArrayList<>();

            if (this.exists()) {
                old_values = FileUtils.getFileContentPerLines(this);
            }

            // Writing old values            
            if (old_values.size() > 0) {
                for (String cont : old_values) {
                    resul += cont + "\n";
                }
                resul += "\n";
            }
        }

        if (this.exists()) {
            this.delete();
        }

        // Writing new values
        resul += "### " + DateUtils.getCurrentTime() + " ###\n";
        for (String message : messages) {
            resul += message + "\n";
        }                
        FileUtils.writeFile(this, resul);
    }

    public void importLog() {        
        if (this.exists()) {
            this.messages.addAll(FileUtils.getFileContentPerLines(this));
        }
    }

    public List<String> getMessages() {
        return this.messages;
    }

    //<editor-fold defaultstate="collapsed" desc="STATIC">
    public static String getLogsFolder() {
        return logs_folder;
    }

    public static void changeLogsFolderTo(String str) {
        logs_folder = str;
    }

    public static final void setDefaultPath(JavaPlugin plugin) {
        PluginLog.default_path = PluginBase.DIRECTORY + logs_folder;
    }

    public static final String getDefaultPath() {
        return PluginLog.default_path;
    }

    /**
     * Returns the values per date.
     * 
     * @param lines Values
     * 
     * @return Values per date
     */
    public static Map<Date, List<String>> getValuesPerDate(List<String> lines) {
        Map<Date, List<String>> data = new HashMap<>();

        Date lastdate = null;
        DateFormat format = DateUtils.getDefaultDateFormat();

        for (String line : lines) {
            if (line.matches("### .* ###")) {
                try {
                    lastdate = format.parse(line.replace("### ", "").replace(" ###", ""));
                    data.put(lastdate, new ArrayList());
                } catch (ParseException ex) {
                    MessageManager.logError(ex.getMessage());
                }
            } else if (!line.isEmpty() && lastdate != null && data.containsKey(lastdate)) {
                data.get(lastdate).add(line);
            }
        }

        return data;
    }
    //</editor-fold>
}
