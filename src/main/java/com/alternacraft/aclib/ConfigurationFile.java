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

        // Seek for "version" or "Version"
        if ((!yaml.contains("version") && !yaml.contains("Version"))
                || (!yaml.getString("version").equals(defaults.getString("version"))
                && !yaml.getString("Version").equals(defaults.getString("version")))) {

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

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.matches("\\s*-\\s?.+") && !linea.contains("#")) {
                    continue;
                }
                String nline = replace(linea, newFile, oldFile);
                fw.write(nline);
            }
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, null, ex);
        }

        if (outFile.exists()) {
            outFile.delete();
        }

        temp.renameTo(outFile);
        MessageManager.log(ChatColor.GREEN + "Previous file settings have been established "
                + "into the new one.");
        MessageManager.log(ChatColor.GREEN + "Just in case, check the result.");
    }

    private String replace(String linea, YamlConfiguration newFile, YamlConfiguration oldFile) {
        String resul = linea;

        for (String value : newFile.getKeys(true)) {
            if (value.equalsIgnoreCase("version")) // This param is sacred
            {
                continue;
            }

            String cValue = value + ":";
            String spaces = ""; // Style

            // I just need the last value for checking
            if (value.contains(".")) {
                String[] vals = value.split("\\.");
                cValue = vals[vals.length - 1] + ":";
                spaces = "    ";
            }

            if (linea.contains(cValue)) {
                Object v;

                if (oldFile.contains(value)) {
                    v = oldFile.get(value);
                } else {
                    v = newFile.get(value);
                }

                resul = spaces + cValue;

                if (v instanceof List) {
                    List<Object> vs = (List<Object>) v;
                    for (Object v1 : vs) {
                        String val = getFilteredString(v1.toString());
                        resul += System.lineSeparator() + spaces + "- " + val;
                    }
                } else if (!(v instanceof MemorySection)) {
                    resul += " " + getFilteredString(v.toString());
                }

                resul += System.lineSeparator();
                break;
            }
        }

        return (resul.equals(linea) ? resul + System.lineSeparator() : resul);
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
