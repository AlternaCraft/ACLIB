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
package com.alternacraft.aclib.commands;

public class CommandArgument {

    private String argument;
    private String description;
    private String usage;

    public CommandArgument(String argument) {
        this.argument = argument;
    }

    public CommandArgument(String argument, String usage) {
        this(argument);
        this.usage = usage;
    }

    public CommandArgument(String argument, String usage, String description) {
        this(argument, usage);        
        this.description = description;
    }

    public String getArgument() {
        return argument;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}