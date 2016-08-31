/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alternacraft.aclib;

import java.util.ArrayList;
import java.util.List;

public class PluginDescription {
    private List<String> lines = new ArrayList();

    public void addLine(String message) {
        lines.add(message);
    }

    public List<String> getLines() {
        return lines;
    }
}
