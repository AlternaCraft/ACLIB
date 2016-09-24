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
package com.alternacraft.aclib;

import com.alternacraft.aclib.config.ConfigDataInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Custom class for working better with the main config file. These are the
 * capabilities:
 * <ul>
 *  <li>Creating config with comments between lines</li>
 *  <li>Checking config version with internal version for checking changes
 *      <ul>
 *          <li>Setting params from previous config into the new one</li>
 *      </ul>
 *  </li>
 *  <li>Saving data from the config into params</li>
 * </ul>
 *
 * @see FileConfiguration
 * @see ConfigDataStore
 */
public class ConfigurationFile {

    /**
     * @since 0.0.8
     */
    public static final String DIRECTORY = new StringBuilder().append(
            PluginBase.INSTANCE.plugin().getDataFolder()).append(
                    File.separator).toString();

    //Attributes
    private JavaPlugin plugin = null;

    private FileConfiguration configFile = null;
    private File backupFile = null;
    
    // Parents
    private String parent;

    // Constructor
    public ConfigurationFile(JavaPlugin plugin) {
        this.plugin = plugin;

        File cfile = new File(new StringBuilder().append(
                DIRECTORY).append(
                        "config.yml").toString());

        if (!cfile.exists() || mismatchVersion(cfile)) {
            plugin.saveDefaultConfig();
            if (backupFile != null) {
                copyParams(cfile);
                backupFile = null;
            }
        }

        plugin.reloadConfig();
        configFile = plugin.getConfig();
    }

    /**
     * Method for loading the main config params
     *
     * @param <T> Loader Interface
     * @param cdi Loader
     *
     * @since 0.0.9
     */
    public <T extends ConfigDataInterface> void loadParams(T cdi) {
        cdi.loadParams(this.configFile);
    }

    // File getter
    public FileConfiguration get() {
        return this.configFile;
    }

    // <editor-fold defaultstate="collapsed" desc="Internal stuff">
    private boolean mismatchVersion(File cFile) {
        File backup = new File(new StringBuilder().append(
                DIRECTORY).append(
                        "config.backup.yml").toString());

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(cFile);
        Configuration defaults = plugin.getConfig().getDefaults();

        // Seeking for "version"
        if (!yaml.contains("version")
                || !yaml.getString("version").equals(defaults.getString("version"))) {

            if (backup.exists()) {
                backup.delete();
            }

            cFile.renameTo(backup);
            MessageManager.log(ChatColor.RED + "Mismatch config version, a new one has been created.");
            backupFile = backup;

            return true;
        }

        return false;
    }

    private void copyParams(File outFile) {
        YamlConfiguration newFile = YamlConfiguration.loadConfiguration(outFile);
        YamlConfiguration oldFile = YamlConfiguration.loadConfiguration(backupFile);

        File temp = new File(new StringBuilder().append(
                DIRECTORY).append(
                        "config_temp.yml").toString());

        try (BufferedReader br = new BufferedReader(new FileReader(outFile));
                FileWriter fw = new FileWriter(temp)) {

            String line;
            while ((line = br.readLine()) != null) {
                // List
                if (line.matches("\\s*-\\s?.+")) {
                    continue;
                }
                String nline = replace(line, newFile, oldFile);
                fw.write(nline);
            }
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, null, ex);
        }

        if (outFile.exists()) {
            outFile.delete();
        }

        temp.renameTo(outFile);
        MessageManager.log(ChatColor.YELLOW + "Previous file settings have been established "
                + "into the new one.");
        MessageManager.log(ChatColor.YELLOW + "Just in case, check the result.");
    }

    private String replace(String line, YamlConfiguration newFile, YamlConfiguration oldFile) {
        // Ignore values
        if (line.contains("version") || line.matches(" *#+.*") || line.isEmpty()
                || line.matches(" +")) {
            return line + System.lineSeparator();
        }

        // Output
        String res;
        
        // ** BEGIN FIND NODE ** //
        String key = getKey(line);
        String cKey = key;
        
        Object v = newFile.get(cKey); // Default value
        
        // Testing with parent
        if (v == null) {
            cKey = parent + "." + key;
            v = newFile.get(cKey);
        }
        
        // Going back
        while (v == null && parent.contains(".")) {
            parent = parent.substring(0, parent.lastIndexOf("."));
            cKey = parent + "." + key;
            v = newFile.get(cKey);
        }
        // ** END FIND NODE ** //

        // Not found??
        if (v == null) {
            return line + System.lineSeparator();
        }

        // Spaces
        String spaces = fillSpaces(cKey.split("\\.").length - 1);

        // Old value <- This is the point
        if (oldFile.contains(cKey)) {
            v = oldFile.get(cKey); // Restore old value
        }

        // Default output [For nodes]
        res = spaces + key + ":";
        
        // Object type
        if (v instanceof List) {
            List<Object> list = (List<Object>) v; // Saving list
            for (Object l : list) {
                String val = getFilteredString(l.toString());
                res += System.lineSeparator() + spaces + "- " + val;
            }
        } else if (v instanceof MemorySection) {
            parent = cKey; // Saving parent
        } else {
            res += " " + getFilteredString(v.toString()); // saving value
        }

        return res += System.lineSeparator(); // Multiple lines
    }

    private String getKey(String str) {
        return str.split(":")[0].replaceAll("\\s+", "");
    }

    private String fillSpaces(int c) {
        String res = "";
        for (int i = 0; i < c; i++) {
            res += "    ";
        }
        return res;
    }

    private String getFilteredString(String str) {
        List<Character> special = Arrays.asList(':', '{', '}', '[', ']', ',', '&',
                '*', '#', '?', '|', '<', '>', '=', '!', '%', '@', '\\');

        for (Character character : special) {
            if (str.contains(String.valueOf(character))) {
                return "\"" + str + "\"";
            }
        }

        return str;
    }
    // </editor-fold>
}
