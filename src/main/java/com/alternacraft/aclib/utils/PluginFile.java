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
import java.io.IOException;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Useful class for managing plugin files.
 *
 * @author AlternaCraft
 */
public class PluginFile extends File {

    public YamlConfiguration yamlFile;

    /**
     * Autocreate file if not exists into default directory.
     *
     * @param path Path to file
     *
     * @since 1.0.2
     */
    public PluginFile(String path) {
        this(PluginBase.DIRECTORY, path, true);
    }

    /**
     * Autocreate file if not exists.
     *
     * @param base Default directory
     * @param path Path to file
     *
     * @since 1.0.2
     */
    public PluginFile(String base, String path) {
        this(base, path, true);
    }

    /**
     * Load file inside default directory.
     *
     * @param path Path to file
     * @param auto_creation Defaults
     *
     * @since 1.0.2
     */
    public PluginFile(String path, boolean auto_creation) {
        this(PluginBase.DIRECTORY, path, auto_creation);
    }

    /**
     * Some extras:
     * <ul>
     *   <li>Create a new file if not exists</li>
     *   <li>Load YAML configuration</li>
     * </ul>
     *
     * @param base Default directory
     * @param path Custom path
     * @param auto_creation Do extras
     *
     * @since 1.0.2
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public PluginFile(String base, String path, boolean auto_creation) {
        super(base + path);
        if (auto_creation && !exists()) {
            createConfig();
        }
    }

    /**
     * @since 1.0.3
     */
    public void createConfig() {
        try {
            this.getParentFile().mkdirs();
            this.createNewFile();
        } catch (IOException ex) {
            MessageManager.logError(ex.getMessage());
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
        return this.getNodes(path, false);
    }
    
    public Set<String> getNodes(String path, boolean deep) {
        return yamlFile.getConfigurationSection(path).getKeys(deep);
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
