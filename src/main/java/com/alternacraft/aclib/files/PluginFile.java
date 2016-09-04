/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginFile extends File {

    public YamlConfiguration yamlFile;

    @Deprecated
    public File file;
    @Deprecated
    public HashMap<String, Object> values = new HashMap<>();

    @Deprecated
    public PluginFile() {
        super("");
    }

    /**
     * @param path String
     * @since 0.0.6
     */
    public PluginFile(String path) {
        super(path);
    }

    @Deprecated
    protected final void loadYamlConfiguration(File file) {
        this.yamlFile = YamlConfiguration.loadConfiguration(file);
    }

    @Deprecated
    protected final void createNewYamlConfiguration(File file) {
        this.yamlFile = new YamlConfiguration();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            setNode(entry.getKey(), entry.getValue());
        }

        saveConfiguration();
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

    @Deprecated
    public void save() {
        try {
            yamlFile.save(file);
        } catch (IOException ex) {
            Logger.getLogger(PluginFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
