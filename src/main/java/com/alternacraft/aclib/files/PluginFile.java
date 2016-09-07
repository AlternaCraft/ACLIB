/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.files;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginFile extends File {

    public YamlConfiguration yamlFile;

    /**
     * @param path String
     * @since 0.0.6
     */
    public PluginFile(String path) {
        super(path);
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
            Logger.getLogger(PluginFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
