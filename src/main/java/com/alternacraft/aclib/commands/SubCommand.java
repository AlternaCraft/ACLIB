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
package com.alternacraft.aclib.commands;

import com.alternacraft.aclib.langs.CommandMessages;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.langs.LangInterface;

/**
 * Class for defining a command argument as a subcommand.
 *
 * @author AlternaCraft
 */
public class SubCommand {

    private final String command;
    private final Enum description;
    private final Condition condition;
    private final String[] aliases;
    private final SubCommand[] arguments;

    public SubCommand(String cmd, Enum desc, Condition condition,
            String[] aliases, SubCommand[] arguments) {
        this.command = cmd;
        this.description = desc;
        this.condition = condition;
        this.aliases = aliases;
        this.arguments = arguments;
    }

    public SubCommand(String command, Enum description) {
        this(command, description, null, new String[0], new SubCommand[0]);
    }

    public SubCommand(String command) {
        this(command, null);
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
     * Returns full command.
     * 
     * @param cmd String command
     * 
     * @return Full command
     */
    public String getFullCommand(String cmd) {
        return cmd + ((!this.command.isEmpty()) ? " " + this.command 
                + ((this.arguments.length > 0) ? " " : "") : "");
    }
    
    /**
     * Returns usage.
     * 
     * @param cmd Main command
     * @param lang Language
     * 
     * @return String with the usage
     */
    public String getUsage(String cmd, Lang lang) {
        return this.getFullCommand(cmd) + ((this.arguments.length > 0) 
                ? (this.arguments.length == 1) ? this.arguments[0].getCommand() : 
                CommandMessages.COMMAND_OPTION.getText(lang) : "");
    }
    
    /**
     * Has description?
     * 
     * @return True if description != null
     */
    public boolean hasDescription() {
        return this.description != null;
    }
    
    /**
     * Returns command description by language.
     *
     * @param <T> Lang enum
     * @param lang Language
     *
     * @return SubCommand description or empty string
     */
    public <T extends Enum & LangInterface> String getDescription(Lang lang) {
        return !this.hasDescription() ? "" : ((T) description).getText(lang);
    }

    /**
     * Returns custom condition
     *
     * @return Condition condition
     */
    public Condition getCondition() {
        return condition;
    }
    
    /**
     * Returns the aliases
     *
     * @return Array of aliases
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Returns all arguments as subcommands
     * 
     * @return Array of subcommands
     */
    public SubCommand[] getArguments() {
        return arguments;
    }    
}
