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

import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.Langs;

/**
 * Class for defining a command argument as a subcommand.
 * 
 * @author AlternaCraft
 */
public class SubCommand {

    private String command;
    private String usage;
    private Enum description;

    public SubCommand(String cmd, String usage, Enum desc) {
        this.command = cmd;
        this.usage = usage;
        this.description = desc;
    }

    /**
     * Gets subcommand
     * 
     * @return SubCommand
     */
    public String getCommand() {
        return command;
    }

    /**
     * Gets command description by language
     * 
     * @param <T> Lang enum
     * @param lang Language
     * 
     * @return SubCommand description
     */
    public <T extends Enum & LangInterface> String getDescription(Langs lang) {
        return ((T) description).getDefaultText(lang);
    }

    /**
     * Gets usage
     * 
     * @return Usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets subcommand
     * 
     * @param cmd Subcommand
     */
    public void setCommand(String cmd) {
        this.command = cmd;
    }

    /**
     * Sets description
     * 
     * @param description Subcommand description
     */
    public void setDescription(Enum description) {
        this.description = description;
    }

    /**
     * Sets usage
     * 
     * @param usage Subcommand usage
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }
}
