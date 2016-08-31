/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib.files;

import java.util.HashMap;

public class PluginFiles {
    public static final PluginFiles instance = new PluginFiles();

    private HashMap<String, PluginFile> files = new HashMap<>();

    private PluginFiles() {
    }

    public void registerFile(String path, PluginFile file) {
        files.put(path, file);
    }

    public PluginFile getFile(String path) {
        return files.get(path);
    }
}
