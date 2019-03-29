/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.aclib.extras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author AlternaCraft
 */
public class SystemCommand {

    /**
     * Executes a command and returns the output and errors, if exists.
     *
     * @param cmd Command
     * @return Array with output and errors, if exists
     * @throws java.io.IOException If something fails.
     */
    public static String[] execute(String cmd) throws IOException {
        String[] result = new String[]{"", ""};

        Process p = Runtime.getRuntime().exec(cmd);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String s;

        while ((s = stdInput.readLine()) != null) {
            result[0] = new StringBuilder(result[0]).append(s).append("\n").toString();
        }
        while ((s = stdError.readLine()) != null) {
            result[1] = new StringBuilder(result[1]).append(s).append("\n").toString();
        }

        p.destroy();

        return result;
    }
}
