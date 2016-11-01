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
import java.io.IOException;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginFile extends File {

    public YamlConfiguration yamlFile;

    /**
     * @param path Path to file
     * 
     * @since 1.0.2
     */
    public PluginFile(String path) {
        this(PluginBase.DIRECTORY, path, true);
    }
    
    /**
     * @param base Default directory
     * @param path Path to file
     * 
     * @since 1.0.2
     */
    public PluginFile(String base, String path) {
        this(base, path, true);
    }

    /**
     * @param path Path to file
     * @param create Create automaticaly the file
     * 
     * @since 1.0.2
     */
    public PluginFile(String path, boolean create) {
        this(PluginBase.DIRECTORY, path, create);
    }

    /**
     * <ul>Some extras:
     * <li>Create a new file if not exists</li>
     * <li>Load YAML configuration</li>
     * </ul>
     * 
     * @param base Default directory
     * @param path Custom path
     * @param create Create automatically the file
     *
     * @since 1.0.2
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public PluginFile(String base, String path, boolean create) {
        super(base + path);

        if (create && !exists()) {
            try {
                createNewFile();
            } catch (IOException ex) {
                MessageManager.log("Couldn't create " + path);
            }
        }

        if (exists()) {
            loadYamlConfiguration();
        }
    }

    /**
     * @since 0.0.6
     */
    public final void loadYamlConfiguration() {
        this.yamlFile = YamlConfiguration.loadConfiguration(this);
    }

    /**
     * @since 0.0.6
     */
    public final void resetYamlConfiguration() {
        this.yamlFile = new YamlConfiguration();
    }

    /**
     * @param yaml YamlConfiguration
     * @since 0.0.6
     */
    public final void copyYamlConfiguration(YamlConfiguration yaml) {
        this.yamlFile = yaml;
    }

    public void setNode(String path, Object value) {
        yamlFile.set(path, value);
    }

    public boolean hasNode(String path) {
        return yamlFile.contains(path);
    }

    public void removeNode(String path) {
        yamlFile.set(path, null);
    }

    public Object getNode(String path) {
        return yamlFile.get(path);
    }

    public Set<String> getNodes(String path) {
        return yamlFile.getConfigurationSection(path).getKeys(false);
    }

    public String getNameWithoutExtension() {
        return this.getName().replaceFirst("[.][^.]+$", "");
    }

    /**
     * @since 0.0.6
     */
    public void saveConfiguration() {
        try {
            yamlFile.save(this);
        } catch (IOException ex) {
            MessageManager.log(ex.getMessage());
        }
    }
}
