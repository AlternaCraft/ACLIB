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
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.Langs;

/**
 * Class for defining a command argument as a subcommand.
 * 
 * @author AlternaCraft
 */
public class SubCommand {

    private final String command;
    private final String usage;
    private final Enum description;
    private final CustomCondition condition;

    public SubCommand(String cmd, String usage, Enum desc, CustomCondition condition) {
        this.command = cmd;
        this.usage = usage;
        this.description = desc;
        this.condition = condition;
    }

    /**
     * Returns subcommand.
     * 
     * @return SubCommand
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns command description by language.
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
     * Returns usage.
     * 
     * @return Usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Returns custom condition
     * 
     * @return Condition condition
     */
    public CustomCondition getCondition() {
        return condition;
    }
}
