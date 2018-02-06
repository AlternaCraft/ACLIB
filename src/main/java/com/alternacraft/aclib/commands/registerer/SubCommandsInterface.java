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
package com.alternacraft.aclib.commands.registerer;

import com.alternacraft.aclib.commands.Condition;
import com.alternacraft.aclib.commands.SubCommand;
import com.alternacraft.aclib.commands.SubCommandArgument;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.commands.SubCommandTabExecutor;

/**
 * Subcommand structure.
 * 
 * @author AlternaCraft
 */
public interface SubCommandsInterface {

    /**
     * Returns subcommand.
     * 
     * @return String with the subcommand
     */
    public String getSubCommand();

    /**
     * Returns description.
     * 
     * @return Enum with the description
     */
    public Enum getDescription();

    /**
     * Returns the custom condition.
     * 
     * @return Condition
     */
    public default Condition getCustomCondition() {
        return null;
    }
    
    /**
     * Returns executor instance.
     * 
     * @return Subcommand
     * 
     * @since 1.0.1
     */
    public SubCommandExecutor getInstance();
    
    /**
     * Returns alias.
     * 
     * @return Array with the alias
     */
    public default String[] getAliases() {
        return new String[0];
    }
    
    /**
     * Returns subcommands.
     * 
     * @return Array with subcommands
     */
    public default SubCommand[] getSubcommands() {
        return new SubCommand[0];
    }
    
    /**
     * Returns arguments.
     * 
     * @return Array with arguments
     */
    public default SubCommandArgument[] getArguments() {
        return new SubCommandArgument[0];
    }
    
    /**
     * Returns subcommand tab executor.
     * 
     * @return SubCommandTabExecutor
     */
    public default SubCommandTabExecutor getTabExecutor() {
        return null;
    }
}
