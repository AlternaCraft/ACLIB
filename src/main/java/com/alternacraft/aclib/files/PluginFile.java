/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginFile {
    public File file;
    public YamlConfiguration yamlFile;
    protected HashMap<String, Object> values = new HashMap<>();

    protected final void loadYamlConfiguration(File file) {
        this.yamlFile = YamlConfiguration.loadConfiguration(file);
    }

    protected final void createNewYamlConfiguration(File file) {
        try {
            this.yamlFile = new YamlConfiguration();

            values.entrySet().stream().forEach((entry) -> {
                this.yamlFile.set(entry.getKey(), entry.getValue());
            });

            this.yamlFile.save(file);
        } catch (IOException ex) {
            Logger.getLogger(PluginFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setNode(String path, Object value) {
        yamlFile.set(path, value);
    }

    public void removeNode(String path) {
        yamlFile.set(path, null);
    }

    public Object getNode(String path) {
        return yamlFile.get(path);
    }
    
    public void save() {
        try {
            yamlFile.save(file);
        } catch (IOException ex) {
            Logger.getLogger(PluginFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
