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

import java.util.ArrayList;
import java.util.List;

/**
 * Custom plugin description
 * 
 * @author AlternaCraft
 */
public class PluginDescription {

    private final List<String> lines = new ArrayList();

    public void addLine(String message) {
        lines.add(message);
    }

    public List<String> getLines() {
        return lines;
    }
}
